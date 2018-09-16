package com.codephrase.android.common

import com.codephrase.android.activity.FrameActivity
import kotlin.reflect.KClass

class NavigationData {
    val type: KClass<out FrameActivity>
    val data: Any?

    constructor(type: KClass<out FrameActivity>) {
        this.type = type
        this.data = null
    }

    constructor(type: KClass<out FrameActivity>, data: Any) {
        this.type = type
        this.data = data
    }
}