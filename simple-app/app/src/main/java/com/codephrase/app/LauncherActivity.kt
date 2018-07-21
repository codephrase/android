package com.codephrase.app

import com.codephrase.android.activity.StartActivity

class LauncherActivity : StartActivity() {
    override fun onResume() {
        super.onResume()

        navigate(MainActivity::class)
    }
}