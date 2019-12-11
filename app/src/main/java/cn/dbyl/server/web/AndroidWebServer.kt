package cn.dbyl.server.web

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response
import java.io.IOException

/**
 * Create by Young on 12/11/2019
 **/
class AndroidWebServer(hostname: String?, port: Int) : NanoHTTPD(hostname, port) {

    override fun serve(session: IHTTPSession?): Response {
        if (session?.method?.equals(Method.POST) == true) {
            val files: Map<String, String> = HashMap()
            val header = session.headers
            try {
                session.parseBody(files)
                val body = session.queryParameterString
                if (body.contains("forward")) {
                    Log.d("YoungTest", "===>Forward")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, null, 0)
            } catch (e: ResponseException) {
                e.printStackTrace()
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, null, 0)
            }

        }
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, null, 0)
    }

}