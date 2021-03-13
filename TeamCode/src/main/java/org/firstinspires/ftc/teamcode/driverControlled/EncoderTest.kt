package org.firstinspires.ftc.teamcode.driverControlled

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.wrappers.Robot

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

        //do stuff
        robot.encodeDrive()

//        robot.goTo(0.5, 1000) //24 inches, 1 tile
        robot.goTo(0.5, 3000)
        sleep(30000)
    }
}
