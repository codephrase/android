package com.codephrase.android.gesture

import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent

open class MoveGestureDetector : BaseGestureDetector {
    private val FOCUS_DELTA_ZERO = PointF()

    private val listener: OnMoveGestureListener

    val focusX: Float
        get() = focusExternal.x

    val focusY: Float
        get() = focusExternal.y

    val focusDelta: PointF
        get() = focusDeltaExternal

    private var previousFocusInternal: PointF? = null
    private var currentFocusInternal: PointF? = null
    private val focusExternal = PointF()
    private var focusDeltaExternal = PointF()

    constructor(context: Context, listener: OnMoveGestureListener) : super(context) {
        this.listener = listener
    }

    override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
        when (actionCode) {
            MotionEvent.ACTION_DOWN -> {
                resetState()

                previousEvent = MotionEvent.obtain(event)
                timeDelta = 0

                updateStateByEvent(event)
            }

            MotionEvent.ACTION_MOVE -> {
                isInProgress = listener.onMoveBegin(this)
            }
        }
    }

    override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
        when (actionCode) {
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                listener.onMoveEnd(this)
                resetState()
            }

            MotionEvent.ACTION_MOVE -> {
                updateStateByEvent(event)

                if (currentPressure / previousPressure > PRESSURE_THRESHOLD) {
                    val updatePrevious = listener.onMove(this)
                    if (updatePrevious) {
                        previousEvent?.recycle()
                        previousEvent = MotionEvent.obtain(event)
                    }
                }
            }
        }
    }

    override fun updateStateByEvent(curr: MotionEvent) {
        super.updateStateByEvent(curr)

        val prev = previousEvent

        currentFocusInternal = determineFocusPoint(curr)

        prev?.let {
            previousFocusInternal = determineFocusPoint(it)
        } ?: run {
            previousFocusInternal = null
        }

        var skipNextMoveEvent = true
        prev?.let {
            skipNextMoveEvent = it.pointerCount != curr.pointerCount
        }

        if (skipNextMoveEvent) {
            focusDeltaExternal = FOCUS_DELTA_ZERO
        } else {
            val prevX = previousFocusInternal?.x ?: 0f
            val prevY = previousFocusInternal?.y ?: 0f

            val currX = currentFocusInternal?.x ?: 0f
            val currY = currentFocusInternal?.y ?: 0f

            focusDeltaExternal = PointF(currX - prevX, currY - prevY)
        }

        focusExternal.x += focusDeltaExternal.x
        focusExternal.y += focusDeltaExternal.y
    }

    private fun determineFocusPoint(e: MotionEvent): PointF {
        val pointerCount = e.pointerCount
        var x = 0f
        var y = 0f

        for (i in 0 until pointerCount) {
            x += e.getX(i)
            y += e.getY(i)
        }

        return PointF(x / pointerCount, y / pointerCount)
    }

    interface OnMoveGestureListener {
        fun onMove(detector: MoveGestureDetector): Boolean

        fun onMoveBegin(detector: MoveGestureDetector): Boolean

        fun onMoveEnd(detector: MoveGestureDetector)
    }

    open class SimpleOnMoveGestureListener : OnMoveGestureListener {
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveBegin(detector: MoveGestureDetector): Boolean {
            return true
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {

        }
    }
}