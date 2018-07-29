package com.codephrase.android.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.codephrase.android.error.NotImplementedError
import kotlin.reflect.KClass

abstract class StartActivity : FrameActivity() {
    protected open val navigationTargetType: KClass<out FrameActivity>
        get() = throw NotImplementedError()

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        viewModel.dataLoaded.observe(this, Observer {
            if (it == true)
                navigate(navigationTargetType)
        })
    }
}