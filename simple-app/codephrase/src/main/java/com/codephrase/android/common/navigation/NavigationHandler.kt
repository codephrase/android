package com.codephrase.android.common.navigation

import android.net.Uri

interface NavigationHandler {
    fun handleUri(uri: Uri): NavigationData?
}