package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Motors.*

class Robot(_env : LinearOpMode){

    private val runtime = ElapsedTime()
    private val env = _env
    
    private lateinit var driver:Map<Motors, DcMotor>
    private lateinit var launcher:Map<Motors, DcMotor>
    private lateinit var servo1:CRServo
    private lateinit var servo2:CRServo

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

        servo1 = env.hardwareMap.get(CRServo::class.java, "servo1")
        servo2 = env.hardwareMap.get(CRServo::class.java, "servo2")


        //set runmodes
        encode(*launcher.values.toTypedArray())
        reverse(driver.getValue(RIGHTFRONT),
                driver.getValue(RIGHTBACK),
                launcher.getValue(RIGHTLAUNCH))

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
    
    fun setLaunchPower(power:Double = 0.0) {
        for ( motor in launcher.values ) {
            motor.power = power
        }
    }

    fun setDrivePower(power:Map<Motors,Double>) {
        for ( motor in power.keys ) {
            driver.getValue(motor).power = power.getValue(motor)
        }
    }

    fun setDrivePower(LF:Double, RF:Double, LB:Double, RB:Double) {
        driver.getValue(LEFTFRONT).power = LF
        driver.getValue(RIGHTFRONT).power = RF
        driver.getValue(LEFTBACK).power = LB
        driver.getValue(RIGHTBACK).power = RB
    }

    fun intake(p: Double){
        servo1.power = p
        servo2.power = -p
    }

    fun setDrivePower(p:Double) {
        setDrivePower(p, p, p, p)
    }

    fun off() {
        setDrivePower(0.0)
    }

}
