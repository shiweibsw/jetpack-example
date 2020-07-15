package com.android.jetpack.viewmodel

import androidx.lifecycle.*
import com.android.jetpack.basiclib.bean.User
import com.android.jetpack.basiclib.http.HttpManager
import com.android.jetpack.basiclib.http.api.callback.OnResultCallBack
import com.android.jetpack.basiclib.http.subscriber.HttpSubscriber
import kotlinx.coroutines.launch

class UserViewModleFactory(val lifecycle: Lifecycle) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Lifecycle::class.java).newInstance(lifecycle)
    }

    /**
     * 有参的ViewModel必须自定义Factory，否则RunTimeException
     * 原因参考：https://blog.csdn.net/qq_43377749/article/details/100856599
     */
    class UserModel(var lifecycle: Lifecycle) : ViewModel() {
        private val users: MutableLiveData<List<User>> by lazy {
            MutableLiveData<List<User>>().also { loadUsers() }
        }

        private val selectedPosition: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

        fun setSelectedPosition(p: Int) {
            selectedPosition.value = p
        }

        fun getSelected() = selectedPosition

        fun getUsers(): LiveData<List<User>> {
            return users
        }

        /**
         * 模拟用户列表，每次请求返回的数据都是不一样的，以此来判断屏幕翻转等Activity重新加载情况下，ViewModle到底有没有重新请求数据
         */
        private fun loadUsers() {
            HttpManager.loadUsers(HttpSubscriber(object : OnResultCallBack<List<User>> {
                override fun onSuccess(t: List<User>) {
                    users.value = t
                }
                override fun onError(code: Int, errorMsg: String) {
                }
            }, lifecycle))
        }
    }
}