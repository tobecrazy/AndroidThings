package cn.dbyl.server.utils

import java.io.IOException
import java.net.ServerSocket
import java.net.Socket


/**
 * Create by young on 11/24/2019
 **/
object SocketServer {

    fun ServerReceviedByTcp(port: Int=1989) { // 声明一个ServerSocket对象
        var serverSocket: ServerSocket? = null
        try { // 创建一个ServerSocket对象，并让这个Socket在1989端口监听
            serverSocket = ServerSocket(port)
            // 调用ServerSocket的accept()方法，接受客户端所发送的请求，
            // 如果客户端没有发送数据，那么该线程就停滞不继续
            val socket: Socket = serverSocket.accept()
            // 从Socket当中得到InputStream对象
            val inputStream = socket.getInputStream()
            val buffer = ByteArray(1024 * 4)
            var temp = 0
            // 从InputStream当中读取客户端所发送的数据
            while (inputStream.read(buffer).also { temp = it } != -1) {
                println(String(buffer, 0, temp))
            }
            serverSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}