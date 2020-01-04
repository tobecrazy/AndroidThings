package cn.dbyl.server.web

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


/**
 * Create by Young on 12/14/2019
 **/
class WebService : Service() {
lateinit var listener:AndroidWebServer.OnDirectionChangeListener
    companion object {
        val TAG: String = "WebService"
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int { // Let it continue running until it is stopped.
        Log.d(TAG, "Service started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service stopped")
    }

}