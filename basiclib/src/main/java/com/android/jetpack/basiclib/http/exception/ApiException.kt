package com.android.jetpack.basiclib.http.exception

class ApiException(resultCode: Int, msg: String) :
    RuntimeException(getApiExceptionMessage(resultCode, msg)) {
    companion object {
        val Code_TimeOut = 1000
        val Code_UnConnected = 1001
        val Code_MalformedJson = 1020
        val Code_Default = 1003
        val CONNECT_EXCEPTION = "网络连接不可用"
        val SOCKET_TIMEOUT_EXCEPTION = "服务器忙，请稍后再试"
        val MALFORMED_JSON_EXCEPTION = "数据解析错误"
    }
}

fun getApiExceptionMessage(resultCode: Int, msg: String): String {

    return "$resultCode#$msg"
}
