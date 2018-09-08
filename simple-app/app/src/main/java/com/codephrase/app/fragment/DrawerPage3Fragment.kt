package com.codephrase.app.fragment

import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.DrawerPage3ViewModel
import kotlin.reflect.KClass

class DrawerPage3Fragment : FrameFragment() {
    override val contentLayoutId: Int
        get() = R.layout.content_drawer_page_3

    override val viewModelType: KClass<out ViewModel>
        get() = DrawerPage3ViewModel::class
}