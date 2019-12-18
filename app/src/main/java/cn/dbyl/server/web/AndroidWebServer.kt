package cn.dbyl.server.web

import android.util.Log
import cn.dbyl.server.data.Constant
import cn.dbyl.server.utils.Direction
import fi.iki.elonen.NanoHTTPD
import java.lang.StringBuilder


/**
 * Create by Young on 12/11/2019
 **/
class AndroidWebServer(hostname: String?, port: Int, var listener: OnDirectionChangeListener) :

    NanoHTTPD(hostname, port) {
    override fun serve(session: IHTTPSession?): Response {
        return if (session?.method?.equals(Method.GET) == true) {
            val parameters = session.parameters
            if (parameters[Constant.DIRECTION] != null && parameters.isNotEmpty()) {
                setDirection(parameters[Constant.DIRECTION]!![0])
                successResponse()
            } else {
                badResponse()
            }
        }else if (session?.method?.equals(Method.POST) == true)
        {
            val parameters = session.parameters
            if (parameters[Constant.DIRECTION] != null && parameters.isNotEmpty()) {
                setDirection(parameters[Constant.DIRECTION]!![0])
                postResponse()
            } else {
                badResponse()
            }
        }
        else {
            response404()
        }
    }

    fun postResponse():Response{
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "{\"status\":\"success\"}")
    }
    private fun successResponse(): Response {
        val sb = StringBuilder()
        sb.append("<!DOCTYPE html>")
        sb.append("<html>")
        sb.append("<head>")
        sb.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"  http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>")
        sb.append("<title>Young's Car Remote control system</title>")
        sb.append("<script type=\"text/javascript\">")
        sb.append("function move(direction) { ")
        sb.append("var button = document.getElementById(direction);")
        sb.append("button.style.backgroundColor = \"#2222ff\";")
        sb.append("window.location = '?direction='+direction; } ")
        sb.append("</script>")
        sb.append("<style>")
        sb.append(
            ".button1 {\n" +
                    "            -webkit-transition-duration: 0.5s;\n" +
                    "            transition-duration: 0.5s;\n" +
                    "            padding: 16px 16px;\n" +
                    "            text-align: center;\n" +
                    "            background-color: yellow;\n" +
                    "            color: black;\n" +
                    "            border: 2px solid #4CAF50;\n" +
                    "            border-radius:5px;\n" +
                    "        }\n" +
                    "        .button1:hover {\n" +
                    "            background-color: #4CAF50;\n" +
                    "            color: white;\n" +
                    "        }"
        )
        sb.append("</style>")
        sb.append("</head>")
        sb.append("<body>")
        sb.append("<div>")
        sb.append("<br>")
        sb.append("<br>")
        sb.append("  <button id=\"Forward\"  class=\"button1\"   onclick=\"move('Forward');\">Forward</button>")
        sb.append("<br>")
        sb.append("<br>")
        sb.append("  <button id=\"Backward\"  class=\"button1\"   onclick=\"move('Backward');\">Backward</button>")
        sb.append("<br>")
        sb.append("<br>")
        sb.append("  <button id=\"Left\"  class=\"button1\"   onclick=\"move('Left');\">Left</button>")
        sb.append("<br>")
        sb.append("<br>")
        sb.append("  <button id=\"Right\"   class=\"button1\"  onclick=\"move('Right');\">Right</button>")
        sb.append("<br>")
        sb.append("<br>")
        sb.append("  <button id=\"Stop\"  class=\"button1\"  onclick=\"move('Stop');\">Stop</button>")
        sb.append("</div>")
        sb.append("</body>")
        sb.append("</html>")
        return newFixedLengthResponse(sb.toString())
    }

    private fun response404(): Response {
        val sb = StringBuilder()
        sb.appendln("<html><body>")
        sb.appendln("<h1>We are sorry, the page you requested cannot be found.</h1>")
        sb.appendln("<h3>The URL may be misspelled or the page you're looking for is no longer available!</h3>")
        sb.appendln("</body></html>")
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, sb.toString())

    }

    fun badResponse(): Response {
        val sb = StringBuilder()
        sb.appendln("<html><body>")
        sb.appendln("<h1>Bad Request!,Please check your request URL.</h1>")
        sb.appendln("</body></html>")
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_HTML, sb.toString())
    }

    interface OnDirectionChangeListener {
        fun onDirectionChanged(direction: Direction?)
    }

    interface OnOpenCamera {
        fun onOpenCamera(cameraIndex: Int?)
    }

    fun setDirection(direction: String) {
        when (direction) {
            Direction.Forward.toString() -> listener.onDirectionChanged(Direction.Forward)
            Direction.Backward.toString() -> listener.onDirectionChanged(Direction.Backward)
            Direction.Left.toString() -> listener.onDirectionChanged(Direction.Left)
            Direction.Right.toString() -> listener.onDirectionChanged(Direction.Right)
            Direction.Stop.toString() -> listener.onDirectionChanged(Direction.Stop)
        }
    }

}