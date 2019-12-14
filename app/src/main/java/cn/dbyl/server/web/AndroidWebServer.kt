package cn.dbyl.server.web

import android.util.Log
import fi.iki.elonen.NanoHTTPD


/**
 * Create by Young on 12/11/2019
 **/
class AndroidWebServer(hostname: String?, port: Int, var listener: OnDirectionChangeListener) :
    NanoHTTPD(hostname, port) {
    override fun serve(session: IHTTPSession?): Response {
        if (session?.method?.equals(Method.GET) == true) {
            Log.d("YoungTest", "===>Get")
            val parameters =
                session.parameters
            if (parameters["direction"] != null) {
                listener.onDirectionChanged(parameters["direction"]!![0])
            }
            val html = "<html><head><script type=\"text/javascript\">" +
                    "  function move(direction) { window.location = '?direction='+direction; }" +
                    "</script></head>" +
                    "<body>" +
                    "  <button onclick=\"move('Forward');\">Forward</button>" +
                    "  <button onclick=\"move('Backward');\">Backward</button>" +
                    "  <button onclick=\"move('Left');\">Left</button>" +
                    "  <button onclick=\"move('Right');\">Right</button>" +
                    "  <button onclick=\"move('Stop');\">Stop</button>" +
                    "</body></html>"
            return newFixedLengthResponse(html)
        } else {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, null, 0)
        }
    }

    interface OnDirectionChangeListener {
        fun onDirectionChanged(direction: String?)
    }

}