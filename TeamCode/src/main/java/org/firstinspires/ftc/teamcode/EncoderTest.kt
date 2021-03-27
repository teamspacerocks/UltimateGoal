package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range

@TeleOp(name = "EncoderTest", group = "Tele") //@Disabled
class EncoderTest : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    private lateinit var robot : Robot

    override fun runOpMode() {

        robot = Robot(this)


        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        robot.encode(robot.arm)
        while(opModeIsActive()) {
            if(gamepad1.left_trigger > 0) {
                robot.encode(robot.arm)
            }
            when {
                gamepad2.dpad_down -> {
                    robot.arm.power = 0.5
                    robot.arm.targetPosition = -400
                }
                gamepad2.dpad_up -> {
//                    if(robot.getArmResistance() < 100.0) {
                        robot.arm.power = 1.0
                        robot.arm.targetPosition = robot.arm.currentPosition + 100
//                    } else {
//                        robot.arm.power = 0.0
//                        robot.encode(robot.arm)
//                    }
                }
                else -> {
                    robot.arm.power = 0.0
                }
            }
            telemetry.addData("arm pos", robot.arm.currentPosition)
            telemetry.update()
        }

    }
}
