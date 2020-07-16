package com.android.jetpack.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity

/**
 * Room 是在Sqlite基础上的一个抽象层
 * 引入：
 *   implementation "androidx.room:room-ktx:$room_version"
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "user-db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            btnAddRecord.setOnClickListener {
            /**
             * 注意所有涉及db的操作均需要异步完成
             */
            doAsync {
                db.userDao().insertUsers(UserEntity("bmob", 25))
            }
        }

        btnLoadAllRecord.setOnClickListener {
            startActivity<WithRoomActivity>()
            doAsync {
                //                db.userDao().getAllUsers().forEach {
//                    info { "==========${it.toString()}" }
//                }
            }
        }

        btnLoadByName.setOnClickListener {
            doAsync {
                var user = db.userDao().loadUserInfo("jerry")
                info { "==========${user.toString()}" }
            }
        }

        btnDeleteRecord.setOnClickListener {
            doAsync {
                var user = db.userDao().deleteUser(UserEntity("jerry", 20))
            }
        }


    }

}
