package com.codephrase.app.fragment

import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.CollapsingPageViewModel
import kotlin.reflect.KClass

class CollapsingPageFragment : FrameFragment() {
    override val toolbarEnabled: Boolean
        get() = true

    override val headerLayoutId: Int
        get() = R.layout.header_collapsing_page

    override val contentLayoutId: Int
        get() = R.layout.content_collapsing_page

    override val viewModelType: KClass<out ViewModel>
        get() = CollapsingPageViewModel::class
}