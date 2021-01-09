package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.tfod.Recognition

@Autonomous(name = "AutonomousTensor", group = "Auto") //@Disabled
class AutonomousTensor : LinearOpMode() {
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

        // run until the end of the match (driver presses STOP)
        sleep(100)
        var recognitions : Array<Recognition> = robot.webcam.see()
        var label: String = "None"

        if (recognitions.size > 0) {
            var bestrec: Recognition = recognitions[0]
            for (recognition in recognitions) {
                if (recognition.confidence > bestrec.confidence) {
                    bestrec = recognition
                }
            }
            label = bestrec.label
        }

        telemetry.addData("type", label)
        telemetry.update()

        robot.travel(0.8, 2000)
        sleep(1000)

        when(label){
            "Single" -> robot.travel(0.8,2000)//stuff
            "Quad"   -> robot.travel(0.8,3000)//stuff
            else     -> robot.travel(0.8,1000)//stuff
        }


    }

}
