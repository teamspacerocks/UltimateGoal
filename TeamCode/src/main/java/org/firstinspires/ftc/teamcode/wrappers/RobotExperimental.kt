package org.firstinspires.ftc.teamcode.wrappers

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.abs


class RobotExperimental(_env: LinearOpMode): Robot(_env) {

    var armDelta : Int
    var armLastLocation : Int
    var deltaTimer : Timer

    init{
        launcher.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        launcher.mode = DcMotor.RunMode.RUN_USING_ENCODER
        armDelta = 0
        armLastLocation = arm.currentPosition
        deltaTimer = Timer("deltaUpdate", true)
        deltaTimer.scheduleAtFixedRate(DeltaUpdate(this), 0, 250)
    }

    class DeltaUpdate(_robot : RobotExperimental) : TimerTask() {
        private val robot = _robot
        override fun run() {
            robot.armDelta = robot.arm.currentPosition - robot.armLastLocation
            robot.armLastLocation = robot.arm.currentPosition
        }

    }

     fun goTo(power: Double,
                      position: Int,
                      targetAngle:Float,
                      busy:Boolean) {
        if(!busy) {
            accelerateTo(power, position, targetAngle)
        } else {
            GlobalScope.launch {
                accelerateTo(power, position, targetAngle)
            }
        }
    }
     private fun accelerateTo(power:Double,
                             position:Int,
                             targetAngle:Float = imu.angularOrientation.firstAngle) {
        //there must be a better way of doing this
        val oldPosition = intArrayOf(driver[0].currentPosition, driver[1].currentPosition, driver[2].currentPosition, driver[3].currentPosition)
        for(i in 0..3) {
            driver[i].targetPosition = oldPosition[i] + position
        }
        val start = env.runtime
        var stage = 0
        var adjustment = 0.0
        while ((stage == 0 || env.runtime - 0.5 < adjustment) && env.opModeIsActive()) {
            if (stage == 0 && abs(driver[0].currentPosition-driver[0].targetPosition) <= 9) {
                stage = 1
                adjustment = env.runtime
            }
            val calculatedPower: Double = abs(power)
                    .coerceAtMost(abs(driver[0].currentPosition - driver[0].targetPosition) / 50.0) //deceleration
                    .coerceAtMost(env.runtime - start) //acceleration
            if (driver[0].targetPosition - driver[0].currentPosition > 0) {
                imudrive(calculatedPower, angle = targetAngle)
            } else {
                imudrive(-calculatedPower, angle = targetAngle)
            }
            env.telemetry.addData("opmode", env.opModeIsActive())
                    .addData("runtime, adjustment","${env.runtime}, $adjustment")

            env.telemetry.update()
        }

        drive(0.0)
    }

    private fun getSum():Int {
        var sum = 0
        for(motor in driver) {
            sum += motor.currentPosition
        }
        return sum
    }



}
