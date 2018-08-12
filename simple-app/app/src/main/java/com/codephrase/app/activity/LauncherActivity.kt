package com.codephrase.app.activity

import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.activity.StartActivity
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.viewmodel.LauncherViewModel
import kotlin.reflect.KClass

class LauncherActivity : StartActivity() {
    override val viewModelType: KClass<out ViewModel>
        get() = LauncherViewModel::class

    override val navigationTargetType: KClass<out FrameActivity>
        get() = FragmentPageActivity::class
}