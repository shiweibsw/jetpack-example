package com.android.jetpack.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync

class UsersViewModel internal constructor(var userRepository: UserDatabase) :
    ViewModel() {

    fun getUsers(): LiveData<MutableList<UserEntity>> {
        return userRepository.userDao().getAllUsers()
    }

    fun insertUser(entity: UserEntity) {
        doAsync {
            userRepository.userDao().insertUsers(entity)
        }
    }

    fun deleteUser(entity: UserEntity) {
        doAsync { userRepository.userDao().deleteUser(entity) }
    }
}