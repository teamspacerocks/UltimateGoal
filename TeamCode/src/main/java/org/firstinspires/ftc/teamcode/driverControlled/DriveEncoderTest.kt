package org.firstinspires.ftc.teamcode.driverControlled

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.wrappers.Robot
import org.firstinspires.ftc.teamcode.wrappers.RobotExperimental

@TeleOp(name = "DriveEncoderTest", group = "Tele") //@Disabled
class DriveEncoderTest : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    private lateinit var robot : Robot

    override fun runOpMode() {

        robot = RobotExperimental(this)


        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        robot.goTo(0.5, 500)

    }
}
