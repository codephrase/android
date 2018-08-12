package com.codephrase.android.viewstate

import android.support.v4.app.Fragment
import java.util.*
import kotlin.reflect.KClass

open class FrameActivityState : ViewState() {
    var navigationStack = Stack<KClass<out Fragment>>()

    var currentFragmentType: KClass<out Fragment>? = null
}