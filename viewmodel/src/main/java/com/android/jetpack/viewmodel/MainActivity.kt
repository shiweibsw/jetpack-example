package com.android.jetpack.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.jetpack.basiclib.bean.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_second.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * ViewModel主要用于存储和管理与UI相关的数据，并且和生命周期相结合
 *
 * 一个非常强大的组件，当你需要不可变数据或者在fragment之间通信时使用这个组件
 *
 * 导入方式
 *  api "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
 *  api "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
 */
class MainActivity : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLoadUser.setOnClickListener {
            /**
             * ViewModelProviders.of 方法已经过时，请使用如下方法
             */
            val model =
            ViewModelProvider(
                this,
                UserViewModleFactory(lifecycle)
            ).get(UserViewModleFactory.UserModel::class.java)
            model.getUsers().observe(this, Observer<List<User>> { users ->
                toast(users.toString())
            })
        }
        btnFragment.setOnClickListener {
            startActivity<FragmentTestActivity>()
        }

    }
}
