package cn.dbyl.server

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import com.leinardi.android.things.driver.hcsr04.Hcsr04SensorDriver
import java.io.IOException
import java.util.*


/**
 */
/**
 * Skeleton of an Android Things activity.
 */
class MainActivity : AppCompatActivity(), SensorEventListener {
    lateinit var buttonGpio4: Gpio
    lateinit var pwm1: Pwm
    lateinit var buttonGpio17: Gpio
    lateinit var buttonGpio23: Gpio
    lateinit var buttonGpio24: Gpio
    lateinit var buttonGpio20: Gpio
    lateinit var buttonGpio26: Gpio
    lateinit var buttonGpio21: Gpio
    lateinit var i2c1: String
    var distance: Int = 11


    private var mHandler: Handler? = null

    private var mProximitySensorDriver: Hcsr04SensorDriver? = null
    private var mSensorManager: SensorManager? = null

    private val mDynamicSensorCallback: DynamicSensorCallback = object : DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            if (sensor.type == Sensor.TYPE_PROXIMITY) {
                mSensorManager!!.registerListener(
                    this@MainActivity,
                    sensor, SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initial()
        initialDistanceCheck("BCM20", "BCM26")

//        pwmCenter()
//
//        Thread.sleep(5000)
//        pwmControl(true)

//        Thread.sleep(5000)
//        pwmControl(false)
//        backward()
//        Thread.sleep(5000)
//        left()
//        Thread.sleep(3000)
//        right()
//        Thread.sleep(13000)
//        stop()


    }

    private fun initialDistanceCheck(trigPin: String, echoPin: String) {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager?.registerDynamicSensorCallback(mDynamicSensorCallback)

        try {
            mProximitySensorDriver = Hcsr04SensorDriver(trigPin, echoPin)
            mProximitySensorDriver?.registerProximitySensor()
        } catch (e: IOException) { // couldn't configure the device...
        }
    }


    private fun initial() {
        val pioService = PeripheralManager.getInstance()
        var list = pioService.gpioList

        pwm1 = pioService.openPwm("PWM1")
        for (str in pioService.i2cBusList) {
            i2c1 = str
        }
        buttonGpio4 = pioService.openGpio("BCM4")
        buttonGpio17 = pioService.openGpio("BCM17")
        buttonGpio23 = pioService.openGpio("BCM23")
        buttonGpio24 = pioService.openGpio("BCM24")
//        buttonGpio20 = pioService.openGpio("BCM20")
//        buttonGpio21 = pioService.openGpio("BCM21")
//        buttonGpio26 = pioService.openGpio("BCM26")
        buttonGpio4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio17.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio23.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio24.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
//        buttonGpio20.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
//        buttonGpio21.setDirection(Gpio.DIRECTION_IN)
//        buttonGpio21.setActiveType(Gpio.ACTIVE_HIGH)
//        buttonGpio21.setEdgeTriggerType(Gpio.EDGE_BOTH)
//        buttonGpio21.registerGpioCallback(mGpioCallback)
//
//        buttonGpio26.setDirection(Gpio.DIRECTION_IN)
//        buttonGpio26.setActiveType(Gpio.ACTIVE_HIGH)
//        buttonGpio26.setEdgeTriggerType(Gpio.EDGE_BOTH)
//        buttonGpio26.registerGpioCallback(mGpioCallback)
    }

    fun direction(a: Boolean, b: Boolean, c: Boolean, d: Boolean) {
        buttonGpio4.value = a
        buttonGpio17.value = b
        buttonGpio23.value = c
        buttonGpio24.value = d
    }

    fun forward() {
        direction(true, false, true, false)
    }


    fun backward() {
        direction(false, true, false, true)
    }

    fun left() {
        direction(true, false, false, false)
    }

    fun right() {
        direction(false, true, false, false)
    }

    fun stop() {
        direction(false, false, false, false)
    }

    fun pwmControl(isLeft: Boolean) {
        var dutyCycle: Double = 0.0
        if (isLeft) {
            dutyCycle = 20.0
        } else {
            dutyCycle = 3.0
        }
        pwm1.setPwmDutyCycle(dutyCycle)
        pwm1.setPwmFrequencyHz(60.0)
        pwm1.setEnabled(true)
    }


    fun pwmCenter() {
        pwm1.setPwmDutyCycle(50.0)
        pwm1.setPwmFrequencyHz(50.0)
        pwm1.setEnabled(true)
        Thread.sleep(2000)
        pwm1.setEnabled(false)
    }


    override fun onDestroy() {
        stopDistance()
        stop()
        pwmCenter()
        super.onDestroy()
    }

    private fun stopDistance() {
        if (mProximitySensorDriver != null) {
            mSensorManager!!.unregisterDynamicSensorCallback(mDynamicSensorCallback)
            mSensorManager!!.unregisterListener(this)
            mProximitySensorDriver!!.unregisterProximitySensor()
            try {
                mProximitySensorDriver!!.close()
            } catch (e: IOException) { // error closing sensor
            } finally {
                mProximitySensorDriver = null
            }
        }
    }

    var mGpioCallback: GpioCallback = object : GpioCallback {
        override fun onGpioEdge(p0: Gpio?): Boolean {
            if (p0 != null) {
                buttonGpio20.value = p0.value
            }
            return true
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        distance = event.values[0].toInt()
        Log.i(
            "YoungTest", "=== $distance"
        )
        if (distance > 15) {
            forward()
        } else {
            stop()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("YoungTest", "sensor accuracy changed: $accuracy")
    }

}
