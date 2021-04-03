package org.firstinspires.ftc.teamcode.auto
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.teamcode.wrappers.Robot
import org.firstinspires.ftc.teamcode.wrappers.RobotExperimental

@Autonomous(name = "TensorRedRight", group = "Auto")
class TensorRedRight : LinearOpMode() {
    private val runtime = ElapsedTime()
    private lateinit var robot : Robot

    override fun runOpMode() {

        robot = RobotExperimental(this)

        telemetry.addData("Status:", "Initialized")
        telemetry.update()

        waitForStart()
        runtime.reset()

        //grab wobble goal
        robot.grab(-0.5)
        sleep(250)

        //turn camera towards stack
        robot.goTo(0.5, 250)
        sleep(500)
        robot.travel(0.0, 500, targetAngle=30.0f)

        //recognize
        robot.webcam.tfod.activate()
        sleep(1000)
        val label = getBestRecognition()

        //turn forward again
        robot.travel(0.0, 500, targetAngle=0.0f)
        sleep(500)
        robot.goTo(0.5, 2000, targetAngle = 0.0f)
        sleep(500)

        //drive forward & turn towards box depending on how many rings
        when(label){
            "None"   -> {
                robot.goTo(0.5, 100)
                robot.turnTo(750, targetAngle = -45.0f)
            }
            "Single" -> {
                robot.goTo(0.5,750, targetAngle = 0.0f)
                sleep(500)
                robot.turnTo(750, targetAngle = 30.0f)
                sleep(500)
                robot.goTo(0.5,250, targetAngle = 30.0f)
            }
            "Quad"   -> {
                robot.goTo(0.5,1750, targetAngle = 0.0f)
                sleep(500)
                robot.turnTo(750, targetAngle = -45.0f)
            }
        }
        sleep(500)

        //bring down the wobble goal
        robot.lift(-0.5)
        sleep(1000)
        robot.lift(0.0)
        robot.grab(1.0)
        sleep(200)
        robot.grab(0.0)

        //turn forward and return to the line
        when(label) {
            "Single" -> {
                robot.turnTo(500, targetAngle = 0.0f)
                sleep(500)
                robot.goTo(0.5,-600, targetAngle=0.0f)
            }
            "Quad" -> {
                robot.turnTo(750, targetAngle=0.0f)
                sleep(500)
                robot.goTo(0.5,-1700, targetAngle=0.0f)
            }
        }
        sleep(500)
        robot.lift(0.5)
        sleep(1250)
        robot.lift(0.0)

        // ????
        when(label){
            "Single" -> {
                robot.goTo(0.5,600, targetAngle = 0.0f)
            }
            "Quad" -> {
                robot.goTo(0.5,1700, targetAngle = 0.0f)
            }
        }



        robot.setLaunchPower(1.0)
        sleep(1000)
        robot.conveyor(1.0)
        sleep(500)
        robot.setLaunchPower(0.0)
        robot.conveyor(0.0)
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
