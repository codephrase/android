package com.codephrase.app.activity

import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.FullscreenPageViewModel
import kotlin.reflect.KClass

class FullscreenPageActivity : FrameActivity() {
    override val contentLayoutId: Int
        get() = R.layout.content_fullscreen_page

    override val viewModelType: KClass<out ViewModel>
        get() = FullscreenPageViewModel::class
}