package cn.dbyl.server

/**
 * Create by young on 12/12/2019
 **/
interface CarRemoteCommand {
    fun forward()
    fun backward()
    fun left()
    fun right()
    fun stop()
}