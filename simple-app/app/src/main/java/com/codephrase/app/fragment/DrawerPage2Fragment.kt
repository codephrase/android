package com.codephrase.app.fragment

import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.DrawerPage2ViewModel
import kotlin.reflect.KClass

class DrawerPage2Fragment : FrameFragment() {
    override val contentLayoutId: Int
        get() = R.layout.content_drawer_page_2

    override val viewModelType: KClass<out ViewModel>
        get() = DrawerPage2ViewModel::class
}