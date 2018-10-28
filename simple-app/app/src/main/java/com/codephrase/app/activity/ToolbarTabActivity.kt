package com.codephrase.app.activity

import androidx.fragment.app.Fragment
import com.codephrase.android.activity.TabActivity
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.fragment.TabPage1Fragment
import com.codephrase.app.fragment.TabPage2Fragment
import com.codephrase.app.fragment.TabPage3Fragment
import com.codephrase.app.viewmodel.ToolbarTabViewModel
import kotlin.reflect.KClass

class ToolbarTabActivity : TabActivity() {
    override val toolbarEnabled: Boolean
        get() = true

    override val headerLayoutId: Int
        get() = R.layout.header_collapsing_page

    override val contentLayoutId: Int
        get() = R.layout.content_tab_page

    override val viewPagerId: Int
        get() = R.id.view_pager

    override val menuId: Int
        get() = R.menu.main

    override val viewModelType: KClass<out ViewModel>
        get() = ToolbarTabViewModel::class

    override fun getItemCount(): Int {
        return 3
    }

    override fun getItemTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "ONE"
            1 -> "TWO"
            2 -> "THREE"
            else -> super.getItemTitle(position)
        }
    }

    override fun getItemView(position: Int): Fragment {
        return when (position) {
            0 -> TabPage1Fragment()
            1 -> TabPage2Fragment()
            2 -> TabPage3Fragment()
            else -> super.getItemView(position)
        }
    }
}