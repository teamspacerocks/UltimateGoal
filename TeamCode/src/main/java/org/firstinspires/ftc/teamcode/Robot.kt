package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Motors.*

class Robot(_env : LinearOpMode){

    private val runtime = ElapsedTime()
    private val env = _env
    
    private val driver: Array<DcMotor>
    private val launcher: Array<DcMotor>

    private val intake: DcMotor
    private val conveyor: DcMotor

    private val webcam: TensorWrapper

    init {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        /*
        * WARNING:
        *
        * The enum Motors contains the index of each motor.
        * After changing this, fix Motor's .i attribute for
        * each Motor.
        *
        * */
        driver = arrayOf(
            getMotor(LF.s),
            getMotor(RF.s),
            getMotor(LB.s),
            getMotor(RB.s)
        )

        launcher = arrayOf(
            getMotor(L_LAUNCH.s),
            getMotor(R_LAUNCH.s)
        )

        intake = getMotor("intake")
        conveyor = getMotor("conveyor")

        webcam = TensorWrapper(env)

        //set runmodes
        encode(*launcher)
        reverse(
            driver[RF.i],
            driver[RB.i],
            launcher[R_LAUNCH.i],
            intake
        )

    }
    
    private fun getMotor(name:String):DcMotor {
        return env.hardwareMap.get(DcMotor::class.java,name)
    }
//     private fun getColorSensor(name:String):ColorSensor {
//         return env.hardwareMap.get(ColorSensor::class.java,name)
//     }
//     private fun getDistanceSensor(name:String):DistanceSensor {
//         return env.hardwareMap.get(DistanceSensor::class.java,name)
//     }
    
    private fun reverse(vararg motors:DcMotor) {
        for ( motor in motors ) {
            motor.direction = DcMotorSimple.Direction.REVERSE
        }
    }
    
    private fun encode(vararg motors:DcMotor) {
        for ( motor in motors ) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }
    }

    fun travel(power: Double = 1.0, seconds: Long){
        drive(power)
        env.sleep(seconds)
        drive(0.0)
    }

    fun launch(power:Double = 0.0) {
        for ( motor in launcher ) {
            motor.power = power
        }
    }

    fun conveyor(p: Double){
        conveyor.power = p
    }
    
    fun drive(power: Array<Double>) {
        for (i in power.indices) {
            driver[i].power = power[i]
        }
    }

    private fun drive(lf:Double, rf:Double, lb:Double, rb:Double) {
        driver[LF.i].power = lf
        driver[RF.i].power = rf
        driver[LB.i].power = lb
        driver[RB.i].power = rb
    }



    fun intake(p: Double){
        intake.power = p
    }

    private fun drive(p:Double) {
        drive(p, p, p, p)
    }

    fun off() {
        drive(0.0)
        intake(0.0)
    }

}
