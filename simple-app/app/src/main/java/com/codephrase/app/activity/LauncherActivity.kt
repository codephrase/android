package com.codephrase.app.activity

import android.net.Uri
import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.activity.StartActivity
import com.codephrase.android.common.navigation.NavigationData
import com.codephrase.android.common.navigation.NavigationHandler
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.viewmodel.LauncherViewModel
import kotlin.reflect.KClass

class LauncherActivity : StartActivity() {
    override val viewModelType: KClass<out ViewModel>
        get() = LauncherViewModel::class

    override val navigationTargetType: KClass<out FrameActivity>
        get() = ToolbarTabActivity::class

    override val navigationHandler: NavigationHandler?
        get() = DeepLinkNavigationHandler()

    class DeepLinkNavigationHandler : NavigationHandler {
        override fun handleUri(uri: Uri): NavigationData? {
            return NavigationData(FullscreenPageActivity::class)
        }
    }
}