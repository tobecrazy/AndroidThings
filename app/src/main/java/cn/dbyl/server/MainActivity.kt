package cn.dbyl.server

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.dbyl.server.utils.Direction
import cn.dbyl.server.utils.GpioBordManager
import cn.dbyl.server.utils.NetWorkUtils
import cn.dbyl.server.web.AndroidWebServer
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import com.leinardi.android.things.driver.hcsr04.Hcsr04
import com.leinardi.android.things.driver.hcsr04.Hcsr04SensorDriver
import com.leinardi.android.things.driver.hd44780.Hd44780
import java.io.IOException
import java.util.*


/**
 * Skeleton of an Android Things activity.
 * 0x7e(PCF8574AT)   0x4e(PCF8574T)
 */
class MainActivity : AppCompatActivity(), SensorEventListener,
    AndroidWebServer.OnDirectionChangeListener {
    lateinit var buttonGpio4: Gpio
    lateinit var pwm0: Pwm
    lateinit var pwm1: Pwm
    lateinit var buttonGpio17: Gpio
    lateinit var buttonGpio21: Gpio
    //car driver
    lateinit var buttonGpio27: Gpio
    lateinit var buttonGpio22: Gpio
    lateinit var buttonGpio23: Gpio
    lateinit var buttonGpio24: Gpio
    lateinit var context: Context
    lateinit var hcsr04:Hcsr04
    var callback: ServiceCallBack = object : ServiceCallBack {
        override fun onBindingService() {

        }

        override fun onUnBindingService() {

        }
    }
    var i2c1: String? = null
    var distance: Int = 11

    private var mHttpServer: AndroidWebServer? = null
    private var mHandler: Handler? = null

    private var mProximitySensorDriver: Hcsr04SensorDriver? = null
    private var mSensorManager: SensorManager? = null
    private lateinit var listener: AndroidWebServer.OnDirectionChangeListener
    private var mLcd: Hd44780? = null


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
        context = this
        startServer(8972)
        initialGpio()
        initalHCSR04()
//        intialLCD()
//        showText("Start")
//        initialDistanceCheck(GpioBordManager.PIN_38_BCM20, GpioBordManager.PIN_37_BCM26)

//        pwmCenter()
    }

    private fun initalHCSR04() {
        try {
            hcsr04 = Hcsr04( GpioBordManager.PIN_37_BCM26,GpioBordManager.PIN_38_BCM20)
        } catch (e: IOException) { // couldn't configure the device...
        }
    }

    private fun intialLCD() {
        mLcd = try {
            Hd44780(
                i2c1,
                Hd44780.I2cAddress.PCF8574T,
                Hd44780.Geometry.LCD_8X1, false
            )
        } catch (e: IOException) {
            Log.e(TAG, "Error while opening LCD", e)
            throw RuntimeException(e)
        }
        Log.d(TAG, "LCD activity created")
    }


    private fun startServer(port: Int) {
        mHttpServer = AndroidWebServer(NetWorkUtils.getLocalIpAddress(this), port, this)
        mHttpServer?.start()
    }

    fun scanDistance() {
        pwmControl(true)
        Thread.sleep(2000)
        pwmControl(false)
    }

    private fun initialDistanceCheck(trigPin: String, echoPin: String) {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager?.registerDynamicSensorCallback(mDynamicSensorCallback)
        try {
            mProximitySensorDriver = Hcsr04SensorDriver(trigPin, echoPin)
            mProximitySensorDriver?.registerProximitySensor()
        } catch (e: IOException) { // couldn't configure the device...
            Log.d(TAG, "couldn't configure the device... ${e.message}")
        }
    }


    private fun initialGpio() {
        Log.d(TAG, "Initial System")
        val pioService = PeripheralManager.getInstance()
        pwm0 = pioService.openPwm(GpioBordManager.getPWMPort(0))
        pwm1 = pioService.openPwm(GpioBordManager.getPWMPort(1))
        i2c1 = GpioBordManager.getI2CPort()
        buttonGpio4 = pioService.openGpio(GpioBordManager.PIN_07_BCM4)
        buttonGpio17 = pioService.openGpio(GpioBordManager.PIN_11_BCM17)
        //left
        buttonGpio27 = pioService.openGpio(GpioBordManager.PIN_13_BCM27)
        buttonGpio22 = pioService.openGpio(GpioBordManager.PIN_15_BCM22)
        //right
        buttonGpio23 = pioService.openGpio(GpioBordManager.PIN_16_BCM23)
        buttonGpio24 = pioService.openGpio(GpioBordManager.PIN_18_BCM24)
        //for distance check
//        buttonGpio21 = pioService.openGpio(GpioBordManager.PIN_40_BCM21)
//        buttonGpio26 = pioService.openGpio(GpioBordManager.PIN_37_BCM26)

        buttonGpio4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio17.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        //for driver
        buttonGpio22.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio27.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio23.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio24.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
    }

    fun direction(a: Boolean, b: Boolean, c: Boolean, d: Boolean) {
        buttonGpio27.value = a
        buttonGpio22.value = b
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
        direction(false, false, true, false)
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
        pwm1.setPwmDutyCycle(60.0)
        pwm1.setPwmFrequencyHz(60.0)
        pwm1.setEnabled(true)
        Thread.sleep(2000)
        pwm1.setEnabled(false)
    }


    override fun onDestroy() {
        mHttpServer?.stop()
//        disableLCD()
        stopDistance()
        stop()
        pwmCenter()
//        stopWeb()
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

    override fun onSensorChanged(event: SensorEvent) {
        Log.i(TAG, String.format(Locale.getDefault(), "sensor changed: [%f]", event.values[0]))

        distance = event.values[0].toInt()

        if (distance < 30) {
            stop()
            scanDistance()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "sensor accuracy changed: $accuracy")
    }

    companion object {
        val TAG = "CarSys"
    }

    override fun onDirectionChanged(direction: Direction?) {
        when (direction) {
            Direction.Forward -> {
//                showText(Direction.Forward.toString())
                forward()
            }
            Direction.Backward -> {
//                showText("Back")
                backward()
            }
            Direction.Left -> {
//                showText(Direction.Left.toString())
                left()
            }
            Direction.Right -> {
//                showText(Direction.Right.toString())
                right()
            }
            Direction.Stop -> {
                showText(Direction.Stop.toString())
                stop()
            }

        }
    }

    fun disableLCD() {
        // disconnect from the display to free resources
        try {
            mLcd!!.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing Hd44780", e)
        } finally {
            mLcd = null
        }
    }

    private fun showText(text: String, second: Long = 3) {
        if (null != mLcd) {
            mLcd!!.setBacklight(true)
            mLcd!!.cursorHome()
            mLcd!!.setBlinkOn(true)
            mLcd!!.clearDisplay()
//            mLcd!!.setCursorOn(true)
            mLcd!!.setDisplayOn(true)
            mLcd!!.scrollDisplayRight()
            mLcd!!.setText(text)
            delay(second)
        }
    }

    private fun delay(second: Long) {
        SystemClock.sleep(second * 1000)
    }

    interface ServiceCallBack {
        fun onBindingService()
        fun onUnBindingService()
    }
}
