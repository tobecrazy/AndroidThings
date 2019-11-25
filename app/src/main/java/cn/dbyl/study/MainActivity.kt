package cn.dbyl.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import kotlin.random.Random


/**
 * 超声波模块的工作原理为，先向TRIG脚输入至少10us的触发信号,
 * 该模块内部将发出 8 个 40kHz 周期电平并检测回波。
 * 一旦检测到有回波信号则ECHO输出高电平回响信号。
 * 回响信号的脉冲宽度与所测的距离成正比。
 * 由此通过发射信号到收到的回响信号时间间隔可以计算得到距离。
 * 公式: 距离=高电平时间*声速(340M/S)/2。
 * <p>
 * <p>
 * VCC,超声波模块电源脚，接5V电源即可
 * Trig，超声波发送脚，高电平时发送出40KHZ出超声波
 * Echo，超声波接收检测脚，当接收到返回的超声波时，输出高电平
 * GND，超声波模块GND
 */
/**
 * Skeleton of an Android Things activity.
 */
class MainActivity : AppCompatActivity() {
    lateinit var buttonGpio4: Gpio
    lateinit var buttonGpio17: Gpio
    lateinit var buttonGpio23: Gpio
    lateinit var buttonGpio24: Gpio
    lateinit var buttonGpio20: Gpio
    lateinit var buttonGpio21: Gpio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initial()
//        forward()
//        Thread.sleep(5000)
//        backward()
//        Thread.sleep(5000)
        left()
//        Thread.sleep(3000)
//        right()
        Thread.sleep(13000)

//        buttonGpio20.setDirection(Gpio.ACTIVE_HIGH)
        stop()
    }

    private fun initial() {
        val pioService = PeripheralManager.getInstance()
        var list = pioService.gpioList
        buttonGpio4 = pioService.openGpio("BCM4")
        buttonGpio17 = pioService.openGpio("BCM17")
        buttonGpio23 = pioService.openGpio("BCM23")
        buttonGpio24 = pioService.openGpio("BCM24")
        buttonGpio20 = pioService.openGpio("BCM20")
        buttonGpio21 = pioService.openGpio("BCM21")
        buttonGpio4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio17.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio23.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio24.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        buttonGpio20.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        buttonGpio21.setDirection(Gpio.DIRECTION_IN)
        buttonGpio21.setActiveType(Gpio.ACTIVE_HIGH)
        buttonGpio21.setEdgeTriggerType(Gpio.EDGE_BOTH)
        buttonGpio21.registerGpioCallback(mGpioCallback)
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

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    var mGpioCallback: GpioCallback = object : GpioCallback {
        override fun onGpioEdge(p0: Gpio?): Boolean {
            if (p0 != null) {
                buttonGpio20.value = p0.value
            }
            return true
        }
    }
}
