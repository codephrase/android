package com.codephrase.android.app

import android.content.Context
import android.provider.Settings
import androidx.multidex.MultiDex
import com.codephrase.android.common.notification.NotificationChannel
import com.codephrase.android.common.notification.NotificationGroup
import com.codephrase.android.helper.StorageHelper
import java.util.*

abstract class Application : android.app.Application() {
    private val SOFTWARE_ID = "software-id"

    internal val hardwareId: String by lazy {
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }

    internal val softwareId: String by lazy {
        var softwareId: String?
        val pref = StorageHelper.getSharedPreference(packageName)

        if (pref.contains(SOFTWARE_ID)) {
            softwareId = pref.getString(SOFTWARE_ID, null)
        } else {
            softwareId = UUID.randomUUID().toString()
            pref.putString(SOFTWARE_ID, softwareId)
        }

        softwareId ?: ""
    }

    open val multiDexEnabled: Boolean
        get() = false

    open val connectTimeout: Int
        get() = 0

    open val readTimeout: Int
        get() = 0

    init {
        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        if (multiDexEnabled)
            MultiDex.install(this)
    }

    open fun onCreateNotificationGroups(): List<NotificationGroup> {
        return arrayListOf()
    }

    open fun onCreateNotificationChannels(): List<NotificationChannel> {
        return arrayListOf()
    }

    companion object {
        internal lateinit var instance: Application
    }
}