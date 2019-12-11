package cn.dbyl.server.utils

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import java.lang.Exception

/**
 * Create by i321533 (young.liu@sap.com) on 12/11/2019
 **/
object GpioBordManager {
    var pioService: PeripheralManager? = null
    private var pwmNames: List<String> = listOf("PWM0", "PWM1")
    private const val DEVICE_RPI3 = "rpi3"
    // pin 3&5  is I2C1
    const val PIN_03 = "BCM2"
    const val PIN_05 = "BCM3"
    //gpio
    const val PIN_07 = "BCM4"
    //8 &10  UART
    const val PIN_08 = "BCM14"
    const val PIN_10 = "BCM15"
    //gpio
    const val PIN_11 = "BCM17"
    //pwm0 I2Si
    const val PIN_12 = "BCM18"
    //gpio
    const val PIN_13 = "BCM27"
    const val PIN_15 = "BCM22"
    const val PIN_16 = "BCM23"
    const val PIN_18 = "BCM24"
    //SPI0
    const val PIN_19 = "BCM10"
    const val PIN_21 = "BCM9"
    //gpio
    const val PIN_22 = "BCM25"
    //SPI0
    const val PIN_23 = "BCM11"
    const val PIN_24 = "BCM8"
    const val PIN_26 = "BCM7"
    //gpio
    const val PIN_29 = "BCM5"
    const val PIN_31 = "BCM6"
    const val PIN_32 = "BCM12"
    //pmw1
    const val PIN_33 = "BCM13"
    //I2S1
    const val PIN_35 = "BCM19"
    //gpio
    const val PIN_36 = "BCM16"

    const val PIN_37 = "BCM26"
    //I2S1
    const val PIN_38 = "BCM20"
    const val PIN_40 = "BCM21"

    init {
        pioService = PeripheralManager.getInstance()

    }

    fun getGpioByPin(pin: String): Gpio? {
        try {
            return pioService?.openGpio(pin)
        } catch (e: Exception) {

        }
        return null
    }

    fun getPWMByIndex(index: Int): Pwm? {
        return when (index) {
            0 -> return pioService?.openPwm(pwmNames[0])
            1 -> return pioService?.openPwm(pwmNames[1])
            else -> null
        }
    }
}