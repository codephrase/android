package com.codephrase.app.activity

import com.codephrase.android.activity.RecyclerActivity
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.ToolbarRecyclerViewModel
import kotlin.reflect.KClass

class ToolbarRecyclerActivity : RecyclerActivity() {
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
        get() = ToolbarRecyclerViewModel::class

    override fun getItemCount(): Int {
        return 3
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.item_recycler_page
    }
}