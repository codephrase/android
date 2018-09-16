package com.codephrase.android.common

import android.net.Uri

interface NavigationHandler {
    fun handleUri(uri: Uri): NavigationData?
}