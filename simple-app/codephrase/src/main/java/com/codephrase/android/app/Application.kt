package com.codephrase.android.app

abstract class Application : android.app.Application() {
    open val connectTimeout: Int
        get() = 0

    open val readTimeout: Int
        get() = 0

    init {
        instance = this
    }

    companion object {
        internal lateinit var instance: Application
    }
}