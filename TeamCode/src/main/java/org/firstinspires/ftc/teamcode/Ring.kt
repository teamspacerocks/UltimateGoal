package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import androidx.core.graphics.ColorUtils

class Ring() {
    
    private lateinit val col:ColorSensor
    private lateinit val dist:DistanceSensor

    fun init() {
        col = getColorSensor("ring")
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
