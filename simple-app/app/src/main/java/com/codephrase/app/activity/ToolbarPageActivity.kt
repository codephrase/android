package com.codephrase.app.activity

import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.ToolbarPageViewModel
import kotlin.reflect.KClass

class ToolbarPageActivity : FrameActivity() {
    override val toolbarEnabled: Boolean
        get() = true

    override val contentLayoutId: Int
        get() = R.layout.content_toolbar_page

    override val viewModelType: KClass<out ViewModel>
        get() = ToolbarPageViewModel::class
}