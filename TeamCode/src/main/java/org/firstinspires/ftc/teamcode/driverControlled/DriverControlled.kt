package org.firstinspires.ftc.teamcode.driverControlled

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.wrappers.Robot
import org.firstinspires.ftc.teamcode.wrappers.RobotExperimental
import kotlin.math.abs

@TeleOp(name = "TeleOp", group = "Tele") //@Disabled
class DriverControlled : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    private lateinit var robot : RobotExperimental
    private var intakePower = 0.0 //for toggle function
    private var shootPower = 0.85
    private var ppos:Int = 0

    override fun runOpMode() {

        robot = RobotExperimental(this)


        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        robot.encode(robot.arm)


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
            val factor = if (gamepad1.right_bumper||gamepad2.right_bumper) 0.5 else 1.0
            val drive = -(gamepad1.left_stick_y.toDouble() + gamepad2.left_stick_y.toDouble()) * factor * factor
            val turn = -(gamepad1.left_stick_x.toDouble() + gamepad2.left_stick_x.toDouble()) * factor
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
            robot.drive(arrayOf(
                    lf,
                    rf,
                    lb,
                    rb,
            ))

            //make this time based or just have a better system
            shootPower += -1*(gamepad2.right_stick_y)*0.001

            // shooter
            robot.setLaunchPower(
                    if(gamepad1.y && gamepad1.x) 0.0
                    else if(gamepad1.y) shootPower
                    else if(gamepad1.x) -shootPower
                    else 0.0
            )

            // intake
            intakePower = when{
                gamepad1.a          ->  0.0
                gamepad1.dpad_up    ->  1.0
                gamepad1.dpad_down  -> -1.0
                else                -> intakePower
            }
            robot.intake(intakePower)
            robot.conveyor(when {
                gamepad1.dpad_up    ->  1.0
                gamepad1.dpad_down  -> -1.0
                else                ->  0.0
            })

            // arm

            if(gamepad2.left_trigger > 0) {
                robot.encode(robot.arm)

            }
            when {
                gamepad2.dpad_down -> {
                    robot.arm.power = -0.75
                    robot.arm.targetPosition = -400
                }
                gamepad2.dpad_up -> {
                    robot.arm.power = 0.75
                    robot.arm.targetPosition = robot.arm.currentPosition + 1000
                }
                abs(robot.armDelta) < 5 -> {
                    robot.arm.power = 0.0
                }
            }




//
//            when {
//                gamepad2.dpad_up    ->  robot.liftPosition(0)
//                gamepad2.dpad_down  ->  robot.liftPosition(-390)
//            }
//            robot.arm.power = Math.abs(robot.arm.currentPosition - robot.arm.targetPosition)/370.0


            robot.grab(when {
                gamepad2.a          -> 1.0
                gamepad2.b &&
                    !gamepad2.start -> -1.0
                else                -> 0.0
            })

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: $runtime")
//            telemetry.addData("powers", "LF: $lf, RF: $rf, LB: $lb, RB: $rb")
            telemetry.addData("shooter: ", shootPower)
            telemetry.addData("armdelta", robot.armDelta).addData("power", robot.arm.power)
            telemetry.addData("currentposition", robot.arm.currentPosition).addData("targepos", robot.arm.targetPosition)
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
