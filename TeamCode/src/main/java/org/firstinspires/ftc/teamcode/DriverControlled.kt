package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range

@TeleOp(name = "TeleOp", group = "Tele") //@Disabled
class DriverControlled : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    private val robot = Robot()

    override fun runOpMode() {

        robot.init(this)
        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Setup a variable for each drive wheel to save power level for telemetry
            var leftPower: Double
            var rightPower: Double

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            val drive = -gamepad1.left_stick_y.toDouble()
            val turn = gamepad1.left_stick_x.toDouble()
            leftPower = Range.clip(drive + turn, -1.0, 1.0)
            rightPower = Range.clip(drive - turn, -1.0, 1.0)

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            robot.rightFront.power = rightPower
            robot.rightBack.power = rightPower
            robot.leftBack.power = leftPower
            robot.leftFront.power = leftPower

            // shooter
            robot.setLaunchPower(
                    if(gamepad2.y && gamepad2.x) 0.0
                    else if(gamepad2.y) 1.0
                    else if(gamepad2.x) -1.0
                    else 0.0
            )


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: $runtime")
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower)
            telemetry.update()
        }

    }
    fun wait(seconds: Double) {
        while (runtime.seconds() < seconds && opModeIsActive()) {
            telemetry.addData("ajsdkf","jeio")
            telemetry.update()
        }
    }
}
