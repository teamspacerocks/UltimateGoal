package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.teamcode.Motors.*


class Robot(_env: LinearOpMode) {

    private val runtime = ElapsedTime()
    private val env = _env

    private val driver: Array<DcMotor>

    private val launcher: DcMotor
    private val intake: DcMotor
    private val conveyor: DcMotor
    private val arm: DcMotor

    private val grabber: CRServo

    val webcam: TensorWrapper
    val imu: BNO055IMU


    var ring: Ring

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

        launcher = getMotor(R_LAUNCH.s)
        intake = getMotor("intake")
        conveyor = getMotor("conveyor")
        arm = getMotor("arm")

        grabber = getServo("grabber")

        webcam = TensorWrapper(env)
        val parameters = BNO055IMU.Parameters()
        parameters.mode = BNO055IMU.SensorMode.IMU
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.loggingEnabled = false

        imu = env.hardwareMap.get(BNO055IMU::class.java, "imu")
        imu.initialize(parameters)

        ring = Ring(getColorSensor("sensor"), getDistanceSensor("sensor"))

        //set runmodes
        //encode(*launcher)
        reverse(
                driver[RF.i],
                driver[RB.i],
                launcher,
                intake,
                arm
        )

    }

    private fun getMotor(name: String): DcMotor {
        return env.hardwareMap.get(DcMotor::class.java, name)
    }

    private fun getColorSensor(name: String): ColorSensor {
        return env.hardwareMap.get(ColorSensor::class.java, name)
    }

    private fun getDistanceSensor(name: String): DistanceSensor {
        return env.hardwareMap.get(DistanceSensor::class.java, name)
    }

    private fun getServo(name: String): CRServo {
        return env.hardwareMap.get(CRServo::class.java, name)
    }

    private fun reverse(vararg motors: DcMotor) {
        for (motor in motors) {
            motor.direction = DcMotorSimple.Direction.REVERSE
        }
    }

    private fun encode(vararg motors: DcMotor) {
        for (motor in motors) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }
    }


    fun travel(power: Double = 1.0,
               ms: Long,
               atime: Long = Math.min(1000,ms),
               useIMU: Boolean = true,
               targetAngle: Float = imu.angularOrientation.firstAngle) {
        GlobalScope.launch {
            accelerate(power, ms, atime, useIMU, targetAngle)
        }
        env.sleep(ms)
        drive(0.0)
    }

    private fun accelerate(power: Double,
                           ms: Long,
                           atime: Long = Math.min(1000, ms),
                           useIMU: Boolean = true,
                           targetAngle: Float = imu.angularOrientation.firstAngle) {
        val start: Double = env.runtime
        while (env.opModeIsActive() && (env.runtime - start) * 1000 <= ms) {
            val apower: Double = Math.min((env.runtime - start) / (atime / 1000.0), power)
            if (useIMU) {
                imudrive(apower, angle = targetAngle)
                env.telemetry.addData("angle", imu.angularOrientation.firstAngle)
                env.telemetry.update()
            } else {
                drive(apower)
            }
        }
        off()
    }

    fun setLaunchPower(power: Double = 0.0) {
        launcher.power = power
    }

    fun conveyor(p: Double) {
        conveyor.power = p
    }

    fun lift(p: Double) {
        arm.power = p
    }

    fun grab(p: Double) {
        grabber.power = p
    }

    fun imudrive(power: Double, icorrection: Double = 40.0, angle: Float = 0.0f) {
        val correction: Double = (imu.angularOrientation.firstAngle.toDouble()- angle)/ icorrection
        val l = power - correction
        val r = power + correction
        drive(l, r, l, r)
    }

    fun drive(power: Array<Double>) {
        drive(power[0], power[1], power[2], power[3])
    }

    fun drive(lf: Double, rf: Double, lb: Double, rb: Double) {
        driver[LF.i].power = lf * 0.9
        driver[RF.i].power = rf
        driver[LB.i].power = lb * 0.9
        driver[RB.i].power = rb
    }

    fun intake(p: Double) {
        intake.power = p
    }

    fun drive(p: Double) {
        drive(p, p, p, p)
    }

    fun off() {
        drive(0.0)
        intake(0.0)
    }

}
