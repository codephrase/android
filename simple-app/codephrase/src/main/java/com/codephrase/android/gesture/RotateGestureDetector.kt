package com.codephrase.android.gesture

import android.content.Context
import android.view.MotionEvent

open class RotateGestureDetector : TwoFingerGestureDetector {
    private val listener: OnRotateGestureListener

    private var sloppyGesture: Boolean = false

    val rotationDegreesDelta: Float
        get() {
            val diffRadians = Math.atan2(previousFingerDiffY.toDouble(), previousFingerDiffX.toDouble()) - Math.atan2(currentFingerDiffY.toDouble(), currentFingerDiffX.toDouble())
            return (diffRadians * 180 / Math.PI).toFloat()
        }

    constructor(context: Context, listener: OnRotateGestureListener) : super(context) {
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
                    isInProgress = listener.onRotateBegin(this)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (sloppyGesture) {
                    sloppyGesture = isSloppyGesture(event)
                    if (!sloppyGesture) {
                        isInProgress = listener.onRotateBegin(this)
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
                    listener.onRotateEnd(this)
                }

                resetState()
            }

            MotionEvent.ACTION_CANCEL -> {
                if (!sloppyGesture) {
                    listener.onRotateEnd(this)
                }

                resetState()
            }

            MotionEvent.ACTION_MOVE -> {
                updateStateByEvent(event)

                if (currentPressure / previousPressure > PRESSURE_THRESHOLD) {
                    val updatePrevious = listener.onRotate(this)
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
    }

    interface OnRotateGestureListener {
        fun onRotate(detector: RotateGestureDetector): Boolean

        fun onRotateBegin(detector: RotateGestureDetector): Boolean

        fun onRotateEnd(detector: RotateGestureDetector)
    }

    open class SimpleOnRotateGestureListener : OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector): Boolean {
            return false
        }

        override fun onRotateBegin(detector: RotateGestureDetector): Boolean {
            return true
        }

        override fun onRotateEnd(detector: RotateGestureDetector) {

        }
    }
}