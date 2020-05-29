package com.android.jetpack.basiclib.http

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.android.jetpack.basiclib.bean.User
import com.android.jetpack.basiclib.http.api.ApiResponse
import com.android.jetpack.basiclib.http.api.ApiService
import com.android.jetpack.basiclib.http.convert.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


@SuppressLint("StaticFieldLeak")
object HttpManager {

    private lateinit var mRetrofit: Retrofit
    private lateinit var mApiService: ApiService
    private var okHttpClient: OkHttpClient
    private val DEFAULT_TIMEOUT: Long = 10L

    init {
        val loggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.i(
                    "HttpManager",
                    message
                )
            })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder().header("Accept", "application/json")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor { chain ->
                var request = chain.request()
                val httpUrl = request.url().newBuilder()
                    .addQueryParameter("p", "z").build()
                request = request.newBuilder().url(httpUrl).build()
                chain.proceed(request)
            }

        okHttpClient = builder.build()
        mRetrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://rap2.taobao.org:38080/app/mock/255859/")
            .client(okHttpClient)
            .build()
        mApiService = mRetrofit.create(ApiService::class.java)
    }

    inline fun <reified T> toSubscribe(o: Observable<ApiResponse<T>>, s: Observer<T>) {
        o.subscribeOn(Schedulers.io())
            .map(Function<ApiResponse<T>, T>() { response ->
                response.getDatas()
            }).unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s)
    }

    fun loadUsers(subscriber: Observer<List<User>>) {
        toSubscribe(mApiService.loadUsers(), subscriber)
    }
}