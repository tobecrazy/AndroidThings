package cn.dbyl.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import kotlin.random.Random


/**
 * Skeleton of an Android Things activity.
 */
class MainActivity : AppCompatActivity() {
    lateinit var pwm0: Pwm
    lateinit var pwm1: Pwm
    lateinit var buttonGpio16: Gpio
    lateinit var buttonGpio26: Gpio
    var isEabled=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pioService = PeripheralManager.getInstance()
        var list = pioService.gpioList


        var pwmList: MutableList<String> = pioService.pwmList
        pwmList.forEach {
            Log.v("YoungTest", "===> $it")
        }
        pwm0 = pioService.openPwm("PWM0")
        pwm1 = pioService.openPwm("PWM1")

        buttonGpio16=pioService.openGpio("BCM16")
        buttonGpio26=pioService.openGpio("BCM26")

        buttonGpio16.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio26.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        buttonGpio16.value=isEabled
        buttonGpio26.value=!isEabled
        pwm0.setPwmFrequencyHz(100.0)
        pwm0.setPwmDutyCycle(100.0)
        pwm1.setPwmFrequencyHz(100.0)
        pwm1.setPwmDutyCycle(100.0)
        pwm0.setEnabled(isEabled)
        pwm1.setEnabled(!isEabled)

        for (i in 1..100) {
            Thread.sleep(2000)
            pwm0.setEnabled(!isEabled)
            pwm1.setEnabled(isEabled)
            buttonGpio16.value=isEabled
            buttonGpio26.value=!isEabled
            isEabled=!isEabled
        }

    }


    override fun onDestroy() {
        pwm0.setEnabled(false)
        pwm1.setEnabled(false)
        super.onDestroy()
    }
}
