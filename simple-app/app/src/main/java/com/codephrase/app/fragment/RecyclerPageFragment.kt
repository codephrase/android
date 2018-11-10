package com.codephrase.app.fragment

import com.codephrase.android.fragment.RecyclerFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.RecyclerPageViewModel
import kotlin.reflect.KClass

class RecyclerPageFragment : RecyclerFragment() {
    override val toolbarEnabled: Boolean
        get() = true

    override val headerLayoutId: Int
        get() = R.layout.header_collapsing_page

    override val contentLayoutId: Int
        get() = R.layout.content_recycler_page

    override val recyclerViewId: Int
        get() = R.id.recycler_view

    override val menuId: Int
        get() = R.menu.main

    override val viewModelType: KClass<out ViewModel>
        get() = RecyclerPageViewModel::class

    override fun getItemCount(): Int {
        return 3
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.item_recycler_page
    }
}