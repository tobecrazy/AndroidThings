package cn.dbyl.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import kotlin.random.Random


/**
 * Skeleton of an Android Things activity.
 */
class MainActivity : AppCompatActivity() {
    lateinit var buttonGpio4: Gpio
    lateinit var buttonGpio17: Gpio
    lateinit var buttonGpio23: Gpio
    lateinit var buttonGpio24: Gpio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initial()
        forward()
        Thread.sleep(5000)
        backward()
        Thread.sleep(5000)
        left()
        Thread.sleep(5000)
        right()
        Thread.sleep(5000)
        stop()
    }

    private fun initial() {
        val pioService = PeripheralManager.getInstance()
        var list = pioService.gpioList
        buttonGpio4 = pioService.openGpio("BCM4")
        buttonGpio17 = pioService.openGpio("BCM17")
        buttonGpio23 = pioService.openGpio("BCM23")
        buttonGpio24 = pioService.openGpio("BCM24")
        buttonGpio4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        buttonGpio17.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        buttonGpio23.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        buttonGpio24.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
    }

    fun direction(a: Boolean, b: Boolean, c: Boolean, d: Boolean) {
        buttonGpio4.value = a
        buttonGpio17.value = b
        buttonGpio23.value = c
        buttonGpio24.value = d
    }

    fun forward()
    {
        direction(true,false,true,false)
    }


    fun backward()
    {
        direction(false,true,false,true)
    }

    fun left()
    {
        direction(true,false,false,true)
    }

    fun right()
    {
        direction(false,true,true,false)
    }

    fun stop()
    {
        direction(false,false,false,false)
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }
}
