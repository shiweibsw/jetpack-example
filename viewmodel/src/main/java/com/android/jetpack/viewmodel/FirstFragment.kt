package com.android.jetpack.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.jetpack.basiclib.bean.User
import com.android.jetpack.viewmodel.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    private val mAdapter by lazy { UserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var model = activity?.run {
            ViewModelProvider(
                this, UserViewModleFactory(lifecycle)
            ).get(UserViewModleFactory.UserModel::class.java)
        }
        model?.let {
            it.getUsers().observe(requireActivity(), Observer<List<User>> { users ->
                mAdapter.setNewData(users)
            })
        }
        mAdapter.bindToRecyclerView(recyclerView)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            model?.let { it.setSelectedPosition(position) }
        }
    }
}
