package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

@Autonomous(name = "Autonomous", group = "Auto") //@Disabled
class Autonomous : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    private val robot = Robot(this)

    override fun runOpMode() {

        robot.init()
        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        // run until the end of the match (driver presses STOP)
        sleep(100)
        robot.setDrivePower(0.8)
        sleep(2000)
        robot.off()

    }

}
