package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import androidx.core.graphics.ColorUtils
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class Ring() {
    
    private val col:ColorSensor
    private val dist:DistanceSensor

    init {
        col = getColorSensor("ring")//TODO: fix lines 14, 15, 24, 29
        dist = getDistanceSensor("ring")
    }

    fun count():Int{
        var hsl = floatArrayOf(0.0f, 0.0f, 0.0f)
        ColorUtils.RGBToHSL(
                col.red(),
                col.green(),
                col.blue(),
                        hsl
        )
        val hue = hsl[0]
        val isYellow = ( hue in 20.0..100.0 )
        if (!isYellow)
            return 0

        when (dist.getDistance(DistanceUnit.INCH)) {
            in 0.5 ..1.0 ->  return 1
            in 2.0 .. 3.0 -> return 3
        }
        return 0
    }
}
