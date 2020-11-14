package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry

class Robot(){
    private val runtime = ElapsedTime()
    lateinit var leftFront:DcMotor
    lateinit var rightFront:DcMotor
    lateinit var leftBack:DcMotor
    lateinit var rightBack:DcMotor

    lateinit var launch1:DcMotor
    lateinit var launch2:DcMotor


    fun init(env : LinearOpMode){

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        leftFront = env.hardwareMap.get(DcMotor::class.java, "left_front")
        rightFront = env.hardwareMap.get(DcMotor::class.java, "right_front")
        leftBack = env.hardwareMap.get(DcMotor::class.java, "left_back")
        rightBack = env.hardwareMap.get(DcMotor::class.java, "right_back")

        launch1 = env.hardwareMap.get(DcMotor::class.java, "launch1")
        launch2 = env.hardwareMap.get(DcMotor::class.java, "launch2")


        //set runmodes
        launch1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launch2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        launch1.mode = DcMotor.RunMode.RUN_USING_ENCODER
        launch2.mode = DcMotor.RunMode.RUN_USING_ENCODER

        //leftFront.direction = DcMotorSimple.Direction.FORWARD
        //leftBack.direction = DcMotorSimple.Direction.FORWARD //Commented out for redundancy

        reverse(rightFront,rightBack,launch2)

    }
    
    private fun reverse(vararg motors:DcMotor) {
        for ( motor in motors ) motor.direction = DcMotorSimple.Direction.REVERSE
    }
    
    fun setLaunchPower(p:Double = 0.0) {
        launch1.power = p
        launch2.power = p
    }
    //a


}
