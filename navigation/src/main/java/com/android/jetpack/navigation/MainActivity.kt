package com.android.jetpack.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Navigation 组件旨在用于具有一个主 Activity 和多个 Fragment 目的地的应用。
 * 主 Activity 与导航图相关联，且包含一个负责根据需要交换目的地的 NavHostFragment。
 * 在具有多个 Activity 目的地的应用中，每个 Activity 均拥有其自己的导航图。
 *
 * Navigation 可以完成以下内容：
 * 1、处理 Fragment 事务。
 * 2、默认情况下，正确处理往返操作。
 * 3、为动画和转换提供标准化资源。
 * 4、实现和处理深层链接。
 * 5、包括导航界面模式（例如抽屉式导航栏和底部导航），用户只需完成极少的额外工作。
 * 6、Safe Args - 可在目标之间导航和传递数据时提供类型安全的 Gradle 插件。
 * 7、ViewModel 支持 - 您可以将 ViewModel 的范围限定为导航图，以在图表的目标之间共享与界面相关的数据。
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }






}
