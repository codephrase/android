package com.codephrase.app.activity

import com.codephrase.android.activity.DrawerActivity
import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.fragment.DrawerHomePageFragment
import com.codephrase.app.fragment.DrawerPage1Fragment
import com.codephrase.app.fragment.DrawerPage2Fragment
import com.codephrase.app.fragment.DrawerPage3Fragment
import com.codephrase.app.viewmodel.ToolbarDrawerViewModel
import kotlin.reflect.KClass

class ToolbarDrawerActivity : DrawerActivity() {
    override val toolbarEnabled: Boolean
        get() = true

    override val menuId: Int
        get() = R.menu.main

    override val navigationHeaderLayoutId: Int
        get() = R.layout.header_drawer

    override val navigationMenuId: Int
        get() = R.menu.drawer

    override val viewModelType: KClass<out ViewModel>
        get() = ToolbarDrawerViewModel::class

    override fun resolveFragmentType(menuId: Int): KClass<out FrameFragment>? {
        return when (menuId) {
            R.id.nav_home -> DrawerHomePageFragment::class
            R.id.nav_page_1 -> DrawerPage1Fragment::class
            R.id.nav_page_2 -> DrawerPage2Fragment::class
            R.id.nav_page_3 -> DrawerPage3Fragment::class
            else -> super.resolveFragmentType(menuId)
        }
    }
}