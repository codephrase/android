package com.codephrase.android.gesture

import android.content.Context
import android.view.MotionEvent

abstract class BaseGestureDetector {
    protected val PRESSURE_THRESHOLD = 0.67f

    protected val context: Context

    var isInProgress: Boolean = false
        protected set

    protected var previousEvent: MotionEvent? = null
    protected var currentEvent: MotionEvent? = null

    var timeDelta: Long = 0L
        protected set

    protected var previousPressure = 0f
    protected var currentPressure = 0f

    val eventTime: Long
        get() = currentEvent?.eventTime ?: 0L

    constructor(context: Context) {
        this.context = context
    }

    fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val actionCode = it.action and MotionEvent.ACTION_MASK

            if (isInProgress)
                handleInProgressEvent(actionCode, it)
            else
                handleStartProgressEvent(actionCode, it)

            return true
        }

        return false
    }

    protected abstract fun handleStartProgressEvent(actionCode: Int, event: MotionEvent)

    protected abstract fun handleInProgressEvent(actionCode: Int, event: MotionEvent)

    protected open fun updateStateByEvent(curr: MotionEvent) {
        val prev = previousEvent

        currentEvent?.recycle()
        currentEvent = MotionEvent.obtain(curr)

        prev?.let {
            timeDelta = curr.eventTime - it.eventTime
        } ?: run {
            timeDelta = 0L
        }

        currentPressure = curr.getPressure(curr.actionIndex)

        prev?.let {
            previousPressure = it.getPressure(it.actionIndex)
        } ?: run {
            previousPressure = 0f
        }
    }

    protected open fun resetState() {
        previousEvent?.let {
            it.recycle()
            previousEvent = null
        }

        currentEvent?.let {
            it.recycle()
            currentEvent = null
        }

        isInProgress = false
    }
}