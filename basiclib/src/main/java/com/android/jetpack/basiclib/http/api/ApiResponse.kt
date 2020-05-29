package com.android.jetpack.basiclib.http.api

class ApiResponse<T> {
  private var status: Int? = null
  private var msg: String? = null
  private var data: T? = null

  constructor(status: Int?, msg: String?, data: T?) {
    this.status = status
    this.msg = msg
    this.data = data
  }

  fun getCode(): Int {
    return status!!
  }

  fun setCode(code: Int) {
    this.status = code
  }

  fun getMsg(): String {
    return msg!!
  }

  fun setMsg(msg: String) {
    this.msg = msg
  }

  fun getDatas(): T {
    return data!!
  }

  fun setDatas(datas: T) {
    this.data = datas
  }

  override fun toString(): String {
    val sb = StringBuffer()
    sb.append("status=$status reason=$msg")
    if (null != data) {
      sb.append(" result:" + data.toString())
    }
    return sb.toString()
  }
}