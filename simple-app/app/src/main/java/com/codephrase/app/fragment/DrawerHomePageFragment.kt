package com.codephrase.app.fragment

import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.DrawerHomePageViewModel
import kotlin.reflect.KClass

class DrawerHomePageFragment : FrameFragment() {
    override val contentLayoutId: Int
        get() = R.layout.content_drawer_home_page

    override val viewModelType: KClass<out ViewModel>
        get() = DrawerHomePageViewModel::class
}