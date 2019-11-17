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
    lateinit var buttonGpio5: Gpio
    lateinit var buttonGpio6: Gpio
    lateinit var buttonGpio12: Gpio
    lateinit var buttonGpio16: Gpio
    lateinit var buttonGpio23: Gpio
    lateinit var buttonGpio24: Gpio
    lateinit var buttonGpio26: Gpio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var pioList: MutableList<String> = pioService.gpioList
//        pioList.forEach { println(it) }

//        var pwmList: MutableList<String> = pioService.pwmList
//        pwmList.forEach{
//            Log.v("YoungTest","===> $it")
//        }
//
//        val pwm = pioService.openPwm("PWM0")
//        pwm.setPwmFrequencyHz(100.0)
//        pwm.setPwmDutyCycle(40.0)
//        var isEnable=false
//        pwm.setEnabled(true)

        initial()

        for (i in 0..1000) {
            Thread.sleep(1000)
            Log.d("YoungTest","${i % 11}")
            display(i % 11)
        }

//
        for (i in 1..1000) {
//            var delay = Random(i)
//            var t = delay.nextLong() % 1000
//            Log.d("YoungTest", t.toString())
//            if (t > 0) {
//                Thread.sleep(t)
//            }
//            isEnable=!isEnable
//            buttonGpio4.value = !buttonGpio4.value
//            buttonGpio16.value = !buttonGpio16.value
//            buttonGpio26.value = !buttonGpio26.value
//              pwm.setEnabled(isEnable)
        }


    }

    private fun initial() {
        val pioService = PeripheralManager.getInstance()
        var list = pioService.gpioList
        buttonGpio4 = pioService.openGpio("BCM4")
        buttonGpio5 = pioService.openGpio("BCM5")
        buttonGpio6 = pioService.openGpio("BCM6")
        buttonGpio12 = pioService.openGpio("BCM12")
        buttonGpio16 = pioService.openGpio("BCM16")
        buttonGpio23 = pioService.openGpio("BCM23")
        buttonGpio24 = pioService.openGpio("BCM24")
        buttonGpio26 = pioService.openGpio("BCM26")
        buttonGpio4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio5.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio6.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio12.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio16.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio23.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio24.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio26.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
    }

    fun displayNone() {
        display(true, true, true, true, true, true, true, true)
    }

    fun display(
        a: Boolean,
        b: Boolean,
        c: Boolean,
        d: Boolean,
        e: Boolean,
        f: Boolean,
        g: Boolean,
        h: Boolean
    ) {
        buttonGpio4.value = !a
        buttonGpio5.value = !b
        buttonGpio6.value = !c
        buttonGpio12.value = !d
        buttonGpio16.value = !e
        buttonGpio23.value = !f
        buttonGpio26.value = !g
        buttonGpio24.value = !h
    }

    fun display0() {
        //0
        displayNone()
        display(true, true, true, false, true, true, true, false)

    }

    fun display1() {
        //1
        displayNone()
        display(true, true, false, false, false, false, false, false)
    }

    fun display2() {
        //2
        displayNone()
        display(false, true, true, true, true, false, true, false)
    }

    fun display3() {
        //3
        displayNone()
        display(false, false, true, true, true, true, true, false)
    }

    fun display4() {
        //4
        display(true, false, false, true, false, true, true, false)
    }

    fun display5() {
        //5
        displayNone()
        display(true, false, true, true, true, true, false, false)
    }

    fun display6() {
        //6
        displayNone()
        display(true, true, true, true, true, true, false, false)
    }

    fun display7() {
        //7
        displayNone()
        display(false, false, false, false, true, true, true, false)
    }

    fun display8() {
        //8
        displayNone()
        display(true, true, true, true, true, true, true, false)

    }

    fun display9() {
        //9
        displayNone()
        display(true, false, true, true, true, true, true, false)
    }

    fun displayDot() {
        //9
        displayNone()
        display(false, false, false, false, false, false, false, true)
    }


    fun display(number: Int) {
        when (number) {
            0 -> display0()
            1 -> display1()
            2 -> display2()
            3 -> display3()
            4 -> display4()
            5 -> display5()
            6 -> display6()
            7 -> display7()
            8 -> display8()
            9 -> display9()
            10 -> displayDot()
        }
    }

    override fun onDestroy() {
        displayNone()
        super.onDestroy()

    }
}
