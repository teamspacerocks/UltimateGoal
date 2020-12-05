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

        val hue = ColorUtils.RGBToHSL(
            col.red(),
            col.green(),
            col.blue()
        )[0]
        val isYellow = ( hue in 20..100 )
        if !isYellow return 0
        
        val distAboveGround = dist.getDistance(DistanceUnit.INCH)
        when (distanceAboveGround) {
            in 0.5 .. 1 ->  return 1
            in 2 .. 3 -> return 3
        }
    }
}
