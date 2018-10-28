package com.codephrase.android.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.constant.NavigationConstants
import kotlin.reflect.KClass

class NavigationHelper private constructor() {
    companion object {
        fun navigateToLauncher() {
            val context = ApplicationHelper.getContext()
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            context.startActivity(intent)
        }

        fun navigateToActivity(type: KClass<out FrameActivity>) {
            navigateToActivity(ApplicationHelper.getContext(), type)
        }

        fun navigateToActivity(type: KClass<out FrameActivity>, data: Any?) {
            navigateToActivity(ApplicationHelper.getContext(), type, data)
        }

        fun navigateToActivity(context: Context, type: KClass<out FrameActivity>) {
            val intent = Intent(context, type.java)
            context.startActivity(intent)
        }

        fun navigateToActivity(context: Context, type: KClass<out FrameActivity>, data: Any?) {
            val intent = Intent(context, type.java)
            intent.putExtra(NavigationConstants.SENDER, context.javaClass)

            data?.let {
                intent.putExtra(NavigationConstants.DATA_TYPE, it.javaClass)
                intent.putExtra(NavigationConstants.DATA_OBJECT, JsonHelper.serialize(it))
            }

            context.startActivity(intent)
        }

        fun navigateToUri(uri: String) {
            navigateToUri(ApplicationHelper.getContext(), uri)
        }

        fun navigateToUri(context: Context, uri: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            context.startActivity(intent)
        }
    }
}