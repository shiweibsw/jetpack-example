package com.android.jetpack.navigation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    enum class AuthenticationState {
        UNAUTHENTCATED,
        AUTHENTICATED,
        INVALID_AUTHENTCATION
    }

    val authenticationState = MutableLiveData<AuthenticationState>()
    var userName: String

    init {
        authenticationState.value = AuthenticationState.UNAUTHENTCATED
        userName = ""
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTCATED
    }

    fun authenticate(userName: String, password: String) {
        if (passwordIsValidForUsername(userName, password)) {
            this.userName = userName
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTCATION
        }
    }

    fun passwordIsValidForUsername(userName: String, password: String): Boolean {
        if (userName == "123" && password == "123")
            return true

        return false
    }


}