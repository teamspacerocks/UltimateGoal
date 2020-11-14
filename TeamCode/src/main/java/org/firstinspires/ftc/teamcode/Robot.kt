package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime

class Robot(_env : LinearOpMode){

    private val runtime = ElapsedTime()
    private val env = _env
    
    private lateinit var driver:Map<String, DcMotor>
    private lateinit var launcher:Map<String, DcMotor>
    lateinit var intake:DcMotor

    fun init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        driver = mapOf(
            "LF" to getMotor("left_front"),
            "RF" to getMotor("right_front"),
            "LB" to getMotor("left_back"),
            "RB" to getMotor("right_back"),
        )

        launcher = mapOf(
            "L" to getMotor("launch1"),
            "R" to getMotor("launch2"),
        )
        
        intake = getMotor("intake")

        //set runmodes
        encode(*launcher.values.toTypedArray())
        reverse(driver["RF"],driver["RB"],launcher["R"])

    }
    
    private fun getMotor(name:String):DcMotor {

        return env.hardwareMap.get(DcMotor::class.java,name)
    }
    
    private fun reverse(vararg motors:DcMotor?) {
        for ( motor in motors ) {
            motor?.direction = DcMotorSimple.Direction.REVERSE
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

    fun setDrivePower(power:Map<String,Double>) {
        for ( location in power.keys ) {
            driver[location]?.power = power[location]!!
        }
    }

}
