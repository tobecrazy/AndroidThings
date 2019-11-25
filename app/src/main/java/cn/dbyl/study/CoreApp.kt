package cn.dbyl.study

import android.app.Application
import android.util.Log
import cn.dbyl.study.utils.NetWorkUtils
import cn.dbyl.study.utils.SocketServer

/**
 * Create by young on 11/24/2019
 **/

class CoreApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val ip = NetWorkUtils.getLocalIpAddress(this)
        Log.d("IP", "Local Ip is ===>$ip")
//        val thread: Thread = Thread(object : Runnable {
//            override fun run() {
//                SocketServer.ServerReceviedByTcp()
//            }
//        })
//        thread.run()
    }
}