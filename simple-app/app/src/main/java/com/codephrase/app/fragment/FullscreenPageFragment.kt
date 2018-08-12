package com.codephrase.app.fragment

import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.FullscreenPageViewModel
import kotlin.reflect.KClass

class FullscreenPageFragment : FrameFragment() {
    override val contentLayoutId: Int
        get() = R.layout.content_fullscreen_page

    override val viewModelType: KClass<out ViewModel>
        get() = FullscreenPageViewModel::class
}