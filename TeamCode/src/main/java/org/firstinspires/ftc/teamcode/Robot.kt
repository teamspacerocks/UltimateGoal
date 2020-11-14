package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry

object Robot{
    private val runtime = ElapsedTime()
    lateinit var leftFront:DcMotor
    lateinit var rightFront:DcMotor
    lateinit var leftBack:DcMotor
    lateinit var rightBack:DcMotor

    lateinit var launch1:DcMotor
    lateinit var launch2:DcMotor


    fun init(){
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        var leftFront:DcMotor = hardwareMap.get(DcMotor::class.java, "left_front")
        var rightFront:DcMotor = hardwareMap.get(DcMotor::class.java, "right_front")
        var leftBack:DcMotor = hardwareMap.get(DcMotor::class.java, "left_back")
        var rightBack:DcMotor = hardwareMap.get(DcMotor::class.java, "right_back")

        var launch1:DcMotor = hardwareMap.get(DcMotor::class.java, "launch1")
        var launch2:DcMotor = hardwareMap.get(DcMotor::class.java, "launch2")

        //leftFront.direction = DcMotorSimple.Direction.FORWARD
        //leftBack.direction = DcMotorSimple.Direction.FORWARD //Commented out for redundancy

        rightFront.direction = DcMotorSimple.Direction.REVERSE
        rightBack.direction = DcMotorSimple.Direction.REVERSE

        launch2.direction = DcMotorSimple.Direction.REVERSE

    }
    fun setLaunchPower(p:Double = 0.0) {
        launch1.power = p
        launch2.power = p
    }
    //a

}
