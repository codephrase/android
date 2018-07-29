package com.codephrase.android.helper

import android.util.TypedValue

class MetricHelper private constructor() {
    companion object {
        fun getPixelFromDp(dp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ApplicationHelper.getDisplayMetrics())
        }
    }
}