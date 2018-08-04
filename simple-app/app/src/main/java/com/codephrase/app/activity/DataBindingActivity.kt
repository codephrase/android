package com.codephrase.app.activity

import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R
import com.codephrase.app.viewmodel.DataBindingViewModel
import kotlin.reflect.KClass

class DataBindingActivity : FrameActivity() {
    override val toolbarEnabled: Boolean
        get() = true

    override val contentLayoutId: Int
        get() = R.layout.content_data_binding

    override val swipeRefreshLayoutId: Int
        get() = R.id.layout_swipe_refresh

    override val viewModelType: KClass<out ViewModel>
        get() = DataBindingViewModel::class
}