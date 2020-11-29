package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Motors.*

class Robot(_env : LinearOpMode){

    private val runtime = ElapsedTime()
    private val env = _env
    
    private lateinit var driver:Map<Motors, DcMotor>
    private lateinit var launcher:Map<Motors, DcMotor>

    private lateinit var intake:DcMotor
    private lateinit var conveyor:DcMotor

    fun init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        driver = mapOf(
            LEFTFRONT to getMotor("left_front"),
            RIGHTFRONT to getMotor("right_front"),
            LEFTBACK to getMotor("left_back"),
            RIGHTBACK to getMotor("right_back"),
        )

        launcher = mapOf(
            LEFTLAUNCH to getMotor("launch1"),
            RIGHTLAUNCH to getMotor("launch2"),
        )

        intake = getMotor("intake")
        conveyor = getMotor("conveyor")

        //set runmodes
        encode(*launcher.values.toTypedArray())
        reverse(driver.getValue(RIGHTFRONT),
                driver.getValue(RIGHTBACK),
                launcher.getValue(RIGHTLAUNCH),
            intake)

    }
    
    private fun getMotor(name:String):DcMotor {

        return env.hardwareMap.get(DcMotor::class.java,name)
    }
    
    private fun reverse(vararg motors:DcMotor) {
        for ( motor in motors ) {
            motor.direction = DcMotorSimple.Direction.REVERSE
        }
    }
    
    private fun encode(vararg motors:DcMotor) {
        for ( motor in motors ) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }
    }

    fun travel(power: Double = 1.0, seconds: Long){
        drive(power)
        env.sleep(seconds)
        drive(0.0)
    }

    fun launch(power:Double = 0.0) {
        for ( motor in launcher.values ) {
            motor.power = power
        }
    }

    fun conveyor(p: Double){
        conveyor.power = p
    }

    fun drive(power:Map<Motors,Double>) {
        for ( motor in power.keys ) {
            driver.getValue(motor).power = power.getValue(motor)
        }
    }

    private fun drive(LF:Double, RF:Double, LB:Double, RB:Double) {
        driver.getValue(LEFTFRONT).power = LF
        driver.getValue(RIGHTFRONT).power = RF
        driver.getValue(LEFTBACK).power = LB
        driver.getValue(RIGHTBACK).power = RB
    }



    fun intake(p: Double){
        intake.power = p
    }

    private fun drive(p:Double) {
        drive(p, p, p, p)
    }

    fun off() {
        drive(0.0)
        intake(0.0)
    }

}
