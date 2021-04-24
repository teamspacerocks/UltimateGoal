@file:Suppress("MemberVisibilityCanBePrivate")

package org.firstinspires.ftc.teamcode.wrappers

import com.qualcomm.robotcore.hardware.Gamepad

class Controller(var gamepad: Gamepad) {
    enum class States{
        PRESS_DOWN, HOLD_DOWN, RELEASE, NEUTRAL
    }
    var pad = gamepad
    private var upPressed = MutibleBool(false)
    private var downPressed = MutibleBool(false)
    private var leftPressed = MutibleBool(false)
    private var rightPressed = MutibleBool(false)

    private class MutibleBool(boolean: Boolean){
        var d = boolean
    }

    companion object{//should try to implement callbacks
        private fun judge(pressed: Boolean, was: MutibleBool): States {
            val result: States
            if (pressed) {
                result = if (was.d)
                    States.HOLD_DOWN
                else
                    States.PRESS_DOWN
                was.d = true

            } else {
                result = if (was.d)
                    States.RELEASE
                else
                    States.NEUTRAL
            }
            return result
        }
    }
    fun getUp(): States {
        return judge(gamepad.dpad_up, upPressed)
    }
    fun getDown(): States{
        return judge(gamepad.dpad_down, downPressed)
    }
    fun getLeft(): States{
        return judge(gamepad.dpad_left, leftPressed)
    }
    fun getRight(): States {
        return judge(gamepad.dpad_right, rightPressed)
    }
}