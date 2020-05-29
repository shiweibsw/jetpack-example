package com.android.jetpack.basiclib.http.api

import com.android.jetpack.basiclib.bean.User
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    @GET("web/view_model")
    fun loadUsers(): Observable<ApiResponse<List<User>>>


}