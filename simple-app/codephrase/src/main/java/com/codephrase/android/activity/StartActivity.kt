package com.codephrase.android.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.codephrase.android.common.navigation.NavigationHandler
import com.codephrase.android.error.NotImplementedError
import kotlin.reflect.KClass

abstract class StartActivity : FrameActivity() {
    protected open val navigationTargetType: KClass<out FrameActivity>
        get() = throw NotImplementedError("navigationTargetType")

    protected open val navigationHandler: NavigationHandler?
        get() = null

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        viewModel.dataLoaded.observe(this, Observer {
            if (it == true) {
                var type = navigationTargetType
                var data: Any? = null

                intent.data?.let {
                    val navigationData = navigationHandler?.handleUri(it)
                    navigationData?.let {
                        type = it.type
                        data = it.data
                    }
                }

                navigate(type, data)
            }
        })
    }
}