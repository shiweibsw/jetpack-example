package com.android.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.android.jetpack.viewmodel.util.SharedPreferencesExtends


class App : Application(), ViewModelStoreOwner {
    companion object {
        var instance: App by SharedPreferencesExtends.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}