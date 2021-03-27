package org.firstinspires.ftc.teamcode.wrappers

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector

class TfodWrapper(env: LinearOpMode) {
    private val TFOD_MODEL_ASSET = "UltimateGoal.tflite"
    private val LABEL_FIRST_ELEMENT = "Quad"
    private val LABEL_SECOND_ELEMENT = "Single"

    private var vuforia: VuforiaLocalizer? = null

    var tfod: TFObjectDetector

    init{

        val parameters = VuforiaLocalizer.Parameters()
        parameters.vuforiaLicenseKey = KeyGet.key
        parameters.cameraName = env.hardwareMap.get(WebcamName::class.java, "Webcam 1")

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters)
        val tfodMonitorViewId = env.hardwareMap.appContext.resources.getIdentifier(
            "tfodMonitorViewId", "id", env.hardwareMap.appContext.packageName
        )
        val tfodParameters = TFObjectDetector.Parameters(tfodMonitorViewId)
        tfodParameters.minResultConfidence = 0.6f
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia)

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT)
    }




    fun see(): Array<Recognition> {
        return tfod.updatedRecognitions.toTypedArray()
    }
}