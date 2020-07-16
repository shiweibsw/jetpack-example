package com.android.jetpack.room

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_with_room.*

class WithRoomActivity : AppCompatActivity() {
    private val viewModel: UsersViewModel by viewModels {
        InjectorUtils.providerUsersViewModelFactory(this)
    }

    private var userStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_room)

        viewModel.getUsers().observe(this, Observer<List<UserEntity>> { users ->
            userStr=""
            users.forEach {
                userStr += it.toString() + "\n"
            }
            tvUsers.text = userStr
        })
        btnAddUser.setOnClickListener {
            viewModel.insertUser(UserEntity("baishiwei", 30))
        }
    }
}
