package com.android.jetpack.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var model = activity?.run {
            ViewModelProvider(
                this, UserViewModleFactory(lifecycle)
            ).get(UserViewModleFactory.UserModel::class.java)
        }

        model?.let {
            it.getSelected().observe(requireActivity(), Observer<Int> { p ->
                var user = it.getUsers().value!![p]
                tvUserInfo.text = "用户名：${user.username},年龄：${user.age}"
            })
        }
    }
}
