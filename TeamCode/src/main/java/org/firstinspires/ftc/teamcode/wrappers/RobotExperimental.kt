package org.firstinspires.ftc.teamcode.wrappers

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.min


class RobotExperimental(_env: LinearOpMode): Robot(_env) {

    var armDelta : Int = 0
    var armLastLocation : Int = arm.currentPosition
    private var deltaTimer : Timer = Timer("deltaUpdate", true)
    private val jobQueue : BlockingQueue<MoveJob> = LinkedBlockingQueue()
    private val worker : Thread = Thread(MoveAsync(this))

    open class MoveJob
    class TravelJob(
        val power: Double,
        val ms: Long,
        val atime: Long = Math.min(1000, ms),
        val useIMU: Boolean = true,
        val targetAngle: Float
    ): MoveJob()
    class GotoJob(
        val power: Double,
        val position: Int,
        val targetAngle: Float
    ): MoveJob()


    init{
        launcher.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcher.mode = DcMotor.RunMode.RUN_USING_ENCODER
        deltaTimer.scheduleAtFixedRate(DeltaUpdate(this), 0, 250)
        worker.isDaemon = true
    }

    class DeltaUpdate(private val robot: RobotExperimental) : TimerTask() {
        override fun run() {
            robot.armDelta = robot.arm.currentPosition - robot.armLastLocation
            robot.armLastLocation = robot.arm.currentPosition
        }

    }

    class MoveAsync(_robot : RobotExperimental): Runnable{
        private val robot = _robot
        override fun run() {
            while(true){
                val j = robot.jobQueue.take()
                if(j is TravelJob){
                    robot.travel(j)
                }else if(j is GotoJob){
                    robot.goTo(j)
                }
                throw RuntimeException("I have no idea how this could happen")
            }
        }
    }

    fun travel(power: Double = 1.0,
               ms: Long,
               atime: Long = min(1000,ms),
               useIMU: Boolean = true,
               targetAngle: Float = imu.angularOrientation.firstAngle,
               async: Boolean = false){
        if(async) {
            jobQueue.add(TravelJob(power, ms, atime, useIMU, targetAngle))

        } else
            super.travel(power, ms, atime, useIMU, targetAngle)
    }
    private fun travel(job : TravelJob){
        super.travel(job.power, job.ms, job.atime, job.useIMU, job.targetAngle)
    }


    fun goTo(power: Double,
             position: Int,
             targetAngle:Float = imu.angularOrientation.firstAngle,
             async: Boolean = false){
        if(async)
            jobQueue.add(GotoJob(power, position, targetAngle))
        else
            super.goTo(power, position, targetAngle)
    }
    private fun goTo(job : GotoJob){
        super.goTo(job.power, job.position, job.targetAngle)
    }

}
