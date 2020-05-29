package com.android.jetpack.basiclib.http.api.callback

interface OnResultCallBack<in T> {

    fun onSuccess(t: T)

    fun onError(code: Int, errorMsg: String)

}