package org.firstinspires.ftc.teamcode
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.tfod.Recognition

@Autonomous(name = "TensorBlueRight", group = "Auto")
@Disabled
class TensorBlueRight : LinearOpMode() {
    private val runtime = ElapsedTime()
    private lateinit var robot : Robot

    override fun runOpMode() {

        robot = Robot(this)

        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        waitForStart()
        runtime.reset()

        robot.webcam.tfod.activate()
        sleep(1000)

        val label = getBestRecognition() // highest confidence recognition type(single, quad, none)

        robot.grab(-0.5)
        sleep(250)
        robot.travel(0.5, 3000, targetAngle = 0.0f)
        sleep(500)

        when(label){
            "None"   -> {
                robot.travel(0.0, 750, targetAngle = 90.0f)
                sleep(500)
                robot.travel(0.5, 500)
            }
            "Single" -> {
                robot.travel(0.5,500, targetAngle = 0.0f)
            }
            "Quad"   -> {
                robot.travel(0.5,1500, targetAngle = 0.0f)
                sleep(500)
                robot.travel(0.0, 750, targetAngle = 90.0f)
                sleep(500)
                robot.travel(0.5, 500)
            }
        }

        robot.lift(-0.5) //BRING down the wobble goal
        sleep(1000)
        robot.lift(0.0)
        robot.grab(1.0)
        sleep(200)
        robot.grab(0.0)

        when(label) {
            "Single" -> {
                robot.travel(-0.5,750)
            }
            "Quad" -> {
                robot.travel(-0.5,2000)
            }
        }
    }

    fun getBestRecognition():String {
        val recognitions : Array<Recognition> = robot.webcam.see()
        if (recognitions.isNotEmpty()) {
            var bestrec: Recognition = recognitions[0]
            for (recognition in recognitions) {
                if (recognition.confidence > bestrec.confidence) {
                    bestrec = recognition
                }
            }
            return bestrec.label
        }
        return "None"
    }

}
