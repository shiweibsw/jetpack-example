package com.android.jetpack.room

import android.content.Context
import androidx.room.Room

object InjectorUtils {

    fun getUserRepository(context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user-db"
        ).build()
    }

    fun providerUsersViewModelFactory(context: Context): UsersViewModelFactory {
        return UsersViewModelFactory(getUserRepository(context))
    }


}