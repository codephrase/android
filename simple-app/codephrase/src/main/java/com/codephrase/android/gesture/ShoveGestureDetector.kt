package com.codephrase.android.gesture

import android.content.Context
import android.view.MotionEvent

open class ShoveGestureDetector : TwoFingerGestureDetector {
    private val listener: OnShoveGestureListener

    private var previousAverageY: Float = 0f
    private var currentAverageY: Float = 0f

    private var sloppyGesture: Boolean = false

    val shovePixelsDelta: Float
        get() = currentAverageY - previousAverageY

    constructor(context: Context, listener: OnShoveGestureListener) : super(context) {
        this.listener = listener
    }

    override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
        when (actionCode) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                resetState()
                previousEvent = MotionEvent.obtain(event)
                timeDelta = 0

                updateStateByEvent(event)

                sloppyGesture = isSloppyGesture(event)
                if (!sloppyGesture) {
                    isInProgress = listener.onShoveBegin(this)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (sloppyGesture) {
                    sloppyGesture = isSloppyGesture(event)
                    if (!sloppyGesture) {
                        isInProgress = listener.onShoveBegin(this)
                    }
                }
            }
        }
    }


    override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
        when (actionCode) {
            MotionEvent.ACTION_POINTER_UP -> {
                updateStateByEvent(event)

                if (!sloppyGesture) {
                    listener.onShoveEnd(this)
                }

                resetState()
            }

            MotionEvent.ACTION_CANCEL -> {
                if (!sloppyGesture) {
                    listener.onShoveEnd(this)
                }

                resetState()
            }

            MotionEvent.ACTION_MOVE -> {
                updateStateByEvent(event)

                if (currentPressure / previousPressure > PRESSURE_THRESHOLD && Math.abs(shovePixelsDelta) > 0.5f) {
                    val updatePrevious = listener.onShove(this)
                    if (updatePrevious) {
                        previousEvent?.recycle()
                        previousEvent = MotionEvent.obtain(event)
                    }
                }
            }
        }
    }

    override fun resetState() {
        super.resetState()
        sloppyGesture = false
        previousAverageY = 0.0f
        currentAverageY = 0.0f
    }

    override fun updateStateByEvent(curr: MotionEvent) {
        super.updateStateByEvent(curr)

        val prev = previousEvent
        val py0 = prev?.getY(0) ?: 0f
        val py1 = prev?.getY(1) ?: 0f
        previousAverageY = (py0 + py1) / 2.0f

        val cy0 = curr.getY(0)
        val cy1 = curr.getY(1)
        currentAverageY = (cy0 + cy1) / 2.0f
    }

    override fun isSloppyGesture(event: MotionEvent): Boolean {
        val sloppy = super.isSloppyGesture(event)
        if (sloppy)
            return true

        val angle = Math.abs(Math.atan2(currentFingerDiffY.toDouble(), currentFingerDiffX.toDouble()))

        return !(0.0f < angle && angle < 0.35f || 2.79f < angle && angle < Math.PI)
    }

    interface OnShoveGestureListener {
        fun onShove(detector: ShoveGestureDetector): Boolean

        fun onShoveBegin(detector: ShoveGestureDetector): Boolean

        fun onShoveEnd(detector: ShoveGestureDetector)
    }

    open class SimpleOnShoveGestureListener : OnShoveGestureListener {
        override fun onShove(detector: ShoveGestureDetector): Boolean {
            return false
        }

        override fun onShoveBegin(detector: ShoveGestureDetector): Boolean {
            return true
        }

        override fun onShoveEnd(detector: ShoveGestureDetector) {

        }
    }
}