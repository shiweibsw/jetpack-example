package com.android.jetpack.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * 数据绑定是一种支持库，借助该库，您可以使用声明性格式将布局中的界面组件绑定到应用中的数据源。
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
