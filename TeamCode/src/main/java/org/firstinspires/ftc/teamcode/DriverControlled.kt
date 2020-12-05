package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.Motors.*

@TeleOp(name = "TeleOp", group = "Tele") //@Disabled
class DriverControlled : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    private val robot = Robot(this)
    private var intakePower = 0.0 //for toggle function

    override fun runOpMode() {

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

            var lf:Double
            var rf:Double
            var lb:Double
            var rb:Double

            var forward:Double
            var right:Double

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            val drive = -gamepad1.left_stick_y.toDouble()
            val turn = -gamepad1.left_stick_x.toDouble()
            leftPower = Range.clip(drive + turn, -1.0, 1.0)
            rightPower = Range.clip(drive - turn, -1.0, 1.0)

            forward = -gamepad1.right_stick_y.toDouble()
            right = -gamepad1.right_stick_x.toDouble()
            lf = forward + right + leftPower
            rf = forward - right + rightPower
            lb = forward - right + leftPower
            rb = forward + right + rightPower

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            robot.drive(mapOf(
                    LEFTFRONT to lf,
                    RIGHTFRONT to rf,
                    LEFTBACK to lb,
                    RIGHTBACK to rb,
            ))

            // shooter
            robot.launch(
                    if(gamepad2.y && gamepad2.x) 0.0
                    else if(gamepad2.y) 1.0
                    else if(gamepad2.x) -1.0
                    else 0.0
            )

            // intake
            intakePower = when{
                gamepad1.dpad_up    ->  1.0
                gamepad1.dpad_down  -> -1.0
                gamepad1.a          ->  0.0
                else                -> intakePower
            }
            robot.intake(intakePower)
            robot.conveyor(when {
                gamepad1.dpad_up    ->  1.0
                gamepad1.dpad_down  -> -1.0
                else                ->  0.0
            })

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: $runtime")
            telemetry.addData("left stick x", gamepad1.left_stick_x.toDouble())
            telemetry.addData("right stick x", gamepad1.right_stick_x.toDouble())
            telemetry.addData("powers", "LF: $lf, RF: $rf, LB: $lb, RB: $rb")
            telemetry.update() 
        }

    }
    fun wait(seconds: Double) {
        while (runtime.seconds() < seconds && opModeIsActive()) {
            telemetry.addData("waiting for","%d seconds", seconds-runtime.seconds())
            telemetry.update()
        }
    }
}
