package com.codephrase.app

import com.codephrase.android.app.Application

class SimpleApplication : Application() {
    override val multiDexEnabled: Boolean
        get() = true
}