package com.codephrase.android.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.codephrase.android.common.NavigationHandler
import com.codephrase.android.error.NotImplementedError
import kotlin.reflect.KClass

abstract class StartActivity : FrameActivity() {
    protected open val navigationTargetType: KClass<out FrameActivity>
        get() = throw NotImplementedError()

    protected open val navigationHandler: NavigationHandler?
        get() = null

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        viewModel.dataLoaded.observe(this, Observer {
            if (it == true) {
                var type = navigationTargetType
                var data: Any? = null

                intent.data?.let { uri ->
                    val navigationData = navigationHandler?.handleUri(uri)
                    navigationData?.let { navigationData ->
                        type = navigationData.type
                        data = navigationData.data
                    }
                }

                navigate(type, data)
            }
        })
    }
}