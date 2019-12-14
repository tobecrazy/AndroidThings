package cn.dbyl.server

import android.app.Application
import android.util.Log
import cn.dbyl.server.utils.NetWorkUtils

/**
 * Create by young on 11/24/2019
 **/

class CoreApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val ip = NetWorkUtils.getLocalIpAddress(this)
        Log.d("IP", "Local Ip is ===>$ip")
    }
}