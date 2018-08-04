package com.codephrase.app.viewmodel

import com.codephrase.android.viewmodel.ViewModel

class CollapsingPageViewModel : ViewModel() {
    override fun initialize() {
        super.initialize()

        title.value = "Collapsing Page"
    }
}