package cn.dbyl.server.utils

import android.os.Build


/**
 * Create by i321533 (young.liu@sap.com) on 12/11/2019
 **/
object GpioBordManager {
    private const val DEVICE_IMX6UL_PICO = "imx6ul_pico"
    private const val DEVICE_IMX7D_PICO = "imx7d_pico"
    private var pwmNames: List<String> = listOf("PWM0", "PWM1")
    private const val DEVICE_RPI3 = "rpi3"
    // pin 3&5  is I2C1
    const val PIN_03_BCM2 = "BCM2"
    const val PIN_05_BCM3 = "BCM3"
    //gpio
    const val PIN_07_BCM4 = "BCM4"
    //8 &10  UART
    const val PIN_08_BCM14 = "BCM14"
    const val PIN_10_BCM15 = "BCM15"
    //gpio
    const val PIN_11_BCM17 = "BCM17"
    //pwm0 I2Si
    const val PIN_12_BCM18 = "BCM18"
    //gpio
    const val PIN_13_BCM27 = "BCM27"
    const val PIN_15_BCM22 = "BCM22"
    const val PIN_16_BCM23 = "BCM23"
    const val PIN_18_BCM24 = "BCM24"
    //SPI0
    const val PIN_19_BCM10 = "BCM10"
    const val PIN_21_BCM9 = "BCM9"
    //gpio
    const val PIN_22_BCM25 = "BCM25"
    //SPI0
    const val PIN_23_BCM11 = "BCM11"
    const val PIN_24_BCM8 = "BCM8"
    const val PIN_26_BCM7 = "BCM7"
    //gpio
    const val PIN_29_BCM5 = "BCM5"
    const val PIN_31_BCM6 = "BCM6"
    const val PIN_32_BCM12 = "BCM12"
    //pmw1
    const val PIN_33_BCM13 = "BCM13"
    //I2S1
    const val PIN_35_BCM19 = "BCM19"
    //gpio
    const val PIN_36_BCM16 = "BCM16"

    const val PIN_37_BCM26 = "BCM26"
    //I2S1
    const val PIN_38_BCM20 = "BCM20"
    const val PIN_40_BCM21 = "BCM21"


    /**
     * Return the preferred I2C port for each board.
     */
    fun getI2CPort(): String? {
        return when (Build.DEVICE) {
            DEVICE_RPI3 -> "I2C1"
            DEVICE_IMX6UL_PICO -> "I2C2"
            DEVICE_IMX7D_PICO -> "I2C1"
            else -> throw IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE)
        }
    }

    fun getPWMPort(index: Int): String? {
        return when (index) {
            0 -> pwmNames[0]
            1 -> pwmNames[1]
            else -> throw IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE)
        }
    }
}