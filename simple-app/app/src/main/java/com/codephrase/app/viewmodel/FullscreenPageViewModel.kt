package com.codephrase.app.viewmodel

import com.codephrase.android.viewmodel.ViewModel

class FullscreenPageViewModel : ViewModel() {
    override fun initialize() {
        super.initialize()

        title.value = "Fullscreen Page"
    }
}