package com.codephrase.android.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import com.codephrase.android.app.Application
import java.util.*

class ApplicationHelper private constructor() {

    companion object {
        fun getContext(): Context {
            return Application.instance
        }

        @Suppress("deprecation")
        fun getLocale(): Locale {
            if (isApiLevel24Supported())
                return getConfiguration().locales[0]
            else
                return getConfiguration().locale
        }

        fun getDisplayMetrics(): DisplayMetrics {
            return getResources().displayMetrics
        }

        fun getConfiguration(): Configuration {
            return getResources().configuration;
        }

        fun getResources(): Resources {
            return getContext().resources
        }

        fun isApiLevel15Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        }

        fun isApiLevel16Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.JELLY_BEAN)
        }

        fun isApiLevel17Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.JELLY_BEAN_MR1)
        }

        fun isApiLevel18Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.JELLY_BEAN_MR2)
        }

        fun isApiLevel19Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.KITKAT)
        }

        fun isApiLevel20Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.KITKAT_WATCH)
        }

        fun isApiLevel21Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.LOLLIPOP)
        }

        fun isApiLevel22Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.LOLLIPOP_MR1)
        }

        fun isApiLevel23Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.M)
        }

        fun isApiLevel24Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.N)
        }

        fun isApiLevel25Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.N_MR1)
        }

        fun isApiLevel26Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.O)
        }

        fun isApiLevel27Supported(): Boolean {
            return isApiSupported(Build.VERSION_CODES.O_MR1)
        }

        private fun isApiSupported(version: Int): Boolean {
            return version <= Build.VERSION.SDK_INT
        }
    }
}