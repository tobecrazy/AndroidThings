package cn.dbyl.study

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver
import com.google.android.things.pio.Gpio
import java.io.IOException


/**
 * Skeleton of an Android Things activity.
 */
class MainActivity : AppCompatActivity(), SensorEventListener {
    lateinit var buttonGpio2: Gpio
    lateinit var buttonGpio3: Gpio
    lateinit var buttonGpio4: Gpio
    lateinit var buttonGpio5: Gpio
    lateinit var buttonGpio6: Gpio
    lateinit var buttonGpio12: Gpio
    lateinit var buttonGpio16: Gpio
    lateinit var buttonGpio23: Gpio
    lateinit var buttonGpio24: Gpio
    lateinit var buttonGpio26: Gpio
    var mTemperatureSensorDriver: Bmx280SensorDriver? = null
    lateinit var mSensorManager: SensorManager

    private val mDynamicSensorCallback: DynamicSensorCallback = object : DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            if (sensor.getType() === Sensor.TYPE_AMBIENT_TEMPERATURE) {
                Log.i("YoungTest", "Temperature sensor connected")
                mSensorManager.registerListener(
                    this@MainActivity
                    , sensor, SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.registerDynamicSensorCallback(mDynamicSensorCallback)

        try {
            mTemperatureSensorDriver = Bmx280SensorDriver("I2C1")
            mTemperatureSensorDriver?.registerTemperatureSensor()
        } catch (e: IOException) {
            Log.e("YoungTest", "Error configuring sensor", e)
        }

//        val pioService = PeripheralManager.getInstance()
//        buttonGpio2 = pioService.openGpio("BCM2")
//        buttonGpio3 = pioService.openGpio("BCM3")
//        println(buttonGpio2.value)
//        println(buttonGpio3.value)
//        Log.d("YoungTest", "===> ${buttonGpio2.value} ")
//        Log.d("YoungTest", "===> ${buttonGpio3.value} ")


    }

    override fun onDestroy() {
        super.onDestroy()
        if (mTemperatureSensorDriver != null) {
            mSensorManager.unregisterDynamicSensorCallback(mDynamicSensorCallback)
            mSensorManager.unregisterListener(this)
            mTemperatureSensorDriver?.unregisterTemperatureSensor()
            try {
                mTemperatureSensorDriver?.close()
            } catch (e: IOException) {
                Log.e("YoungTest", "Error closing sensor", e)
            } finally {
                mTemperatureSensorDriver = null

            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.i("YoungTest", "sensor accuracy changed: $p1");
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.i("YoungTest", "sensor changed: " + (p0?.values?.get(0) ?: 0))
    }
}
