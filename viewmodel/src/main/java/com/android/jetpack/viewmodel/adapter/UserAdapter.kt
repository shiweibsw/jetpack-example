package com.android.jetpack.viewmodel.adapter

import com.android.jetpack.basiclib.bean.User
import com.android.jetpack.viewmodel.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class UserAdapter(resId: Int = R.layout.item_user) :
    BaseQuickAdapter<User, BaseViewHolder>(resId) {
    override fun convert(helper: BaseViewHolder, item: User) {
        helper.setText(R.id.username, "${item.username}")
        helper.setText(R.id.age, "${item.age}")
    }
}