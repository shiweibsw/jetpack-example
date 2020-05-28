package com.android.jetpack.example

import androidx.databinding.ObservableField
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    var firstName: ObservableField<String> = ObservableField<String>()
    , var lastName: String
)

