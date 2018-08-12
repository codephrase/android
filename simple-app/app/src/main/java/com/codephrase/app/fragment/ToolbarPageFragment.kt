package com.codephrase.app.fragment

import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.ToolbarPageViewModel
import kotlin.reflect.KClass

class ToolbarPageFragment : FrameFragment() {
    override val toolbarEnabled: Boolean
        get() = true

    override val contentLayoutId: Int
        get() = R.layout.content_toolbar_page

    override val viewModelType: KClass<out ViewModel>
        get() = ToolbarPageViewModel::class
}