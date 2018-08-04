package com.codephrase.app.viewmodel

import com.codephrase.android.viewmodel.ViewModel

class ToolbarPageViewModel : ViewModel() {
    override fun initialize() {
        super.initialize()

        title.value = "Toolbar Page"
    }
}