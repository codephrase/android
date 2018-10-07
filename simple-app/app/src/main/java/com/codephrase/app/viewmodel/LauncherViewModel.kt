package com.codephrase.app.viewmodel

import com.codephrase.android.helper.NotificationHelper
import com.codephrase.android.viewmodel.ViewModel

class LauncherViewModel : ViewModel() {
    override fun onDataLoading() {
        try {
            NotificationHelper.initialize()
        } catch (e: InterruptedException) {

        }

        onDataLoaded()
    }
}