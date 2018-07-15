package com.codephrase.android.helper

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.TypedValue

class ResourceHelper private constructor() {

    companion object {
        fun getString(resId: Int): String {
            return ApplicationHelper.getResources().getString(resId)
        }

        fun getString(resId: Int, vararg formatArgs: Any): String {
            return ApplicationHelper.getResources().getString(resId, formatArgs)
        }

        fun getStringArray(resId: Int): Array<String> {
            return ApplicationHelper.getResources().getStringArray(resId)
        }

        fun getPlurals(resId: Int, quantity: Int): String {
            return ApplicationHelper.getResources().getQuantityString(resId, quantity)
        }

        fun getPlurals(resId: Int, quantity: Int, vararg formatArgs: Any): String {
            return ApplicationHelper.getResources().getQuantityString(resId, quantity, formatArgs)
        }

        fun getBoolean(resId: Int): Boolean {
            return ApplicationHelper.getResources().getBoolean(resId)
        }

        fun getInt(resId: Int): Int {
            return ApplicationHelper.getResources().getInteger(resId)
        }

        fun getIntArray(resId: Int): IntArray {
            return ApplicationHelper.getResources().getIntArray(resId)
        }

        fun getTypedArray(resId: Int): TypedArray {
            return ApplicationHelper.getResources().obtainTypedArray(resId)
        }

        fun getDimension(resId: Int): Float {
            return ApplicationHelper.getResources().getDimension(resId)
        }

        fun getColor(resId: Int): Int {
            return ContextCompat.getColor(ApplicationHelper.getContext(), resId)
        }

        fun getColorStateList(resId: Int): ColorStateList? {
            return ContextCompat.getColorStateList(ApplicationHelper.getContext(), resId)
        }

        fun getDrawable(resId: Int): Drawable? {
            return ContextCompat.getDrawable(ApplicationHelper.getContext(), resId)
        }

        fun getTheme(context: Context, resId: Int): Int {
            val typedValue = TypedValue()
            val theme = context.getTheme()
            theme.resolveAttribute(resId, typedValue, true)
            return typedValue.data
        }
    }
}