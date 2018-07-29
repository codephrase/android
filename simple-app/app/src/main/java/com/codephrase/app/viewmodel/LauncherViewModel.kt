package com.codephrase.app.viewmodel

import com.codephrase.android.viewmodel.ViewModel

class LauncherViewModel : ViewModel() {
    override fun onDataLoading() {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {

        }

        onDataLoaded()
    }
}