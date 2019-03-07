package com.codephrase.android.gesture

import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration

abstract class TwoFingerGestureDetector : BaseGestureDetector {
    private var edgeSlop: Float = 0f

    protected var previousFingerDiffX: Float = 0f
    protected var previousFingerDiffY: Float = 0f
    protected var currentFingerDiffX: Float = 0f
    protected var currentFingerDiffY: Float = 0f

    private var previousLen: Float = 0f
    private var currentLen: Float = 0f

    val previousSpan: Float
        get() {
            if (previousLen == -1f) {
                val pvx = previousFingerDiffX
                val pvy = previousFingerDiffY
                previousLen = Math.sqrt((pvx * pvx + pvy * pvy).toDouble()).toFloat()
            }
            return previousLen
        }

    val currentSpan: Float
        get() {
            if (currentLen == -1f) {
                val cvx = currentFingerDiffX
                val cvy = currentFingerDiffY
                currentLen = Math.sqrt((cvx * cvx + cvy * cvy).toDouble()).toFloat()
            }
            return currentLen
        }

    constructor(context: Context) : super(context) {
        val config = ViewConfiguration.get(context)
        edgeSlop = config.scaledEdgeSlop.toFloat()
    }

    abstract override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent)

    abstract override fun handleInProgressEvent(actionCode: Int, event: MotionEvent)

    override fun updateStateByEvent(curr: MotionEvent) {
        super.updateStateByEvent(curr)

        val prev = previousEvent

        currentLen = -1f
        previousLen = -1f

        val px0 = prev?.getX(0) ?: 0f
        val py0 = prev?.getY(0) ?: 0f
        val px1 = prev?.getX(1) ?: 0f
        val py1 = prev?.getY(1) ?: 0f
        val pvx = px1 - px0
        val pvy = py1 - py0
        previousFingerDiffX = pvx
        previousFingerDiffY = pvy

        val cx0 = curr.getX(0)
        val cy0 = curr.getY(0)
        val cx1 = curr.getX(1)
        val cy1 = curr.getY(1)
        val cvx = cx1 - cx0
        val cvy = cy1 - cy0
        currentFingerDiffX = cvx
        currentFingerDiffY = cvy
    }

    protected fun getRawX(event: MotionEvent, pointerIndex: Int): Float {
        val offset = event.x - event.rawX

        if (pointerIndex < event.pointerCount)
            return event.getX(pointerIndex) + offset
        else
            return 0f
    }

    protected fun getRawY(event: MotionEvent, pointerIndex: Int): Float {
        val offset = event.y - event.rawY

        if (pointerIndex < event.pointerCount)
            return event.getY(pointerIndex) + offset
        else
            return 0f
    }

    protected open fun isSloppyGesture(event: MotionEvent): Boolean {
        val metrics = context.resources.displayMetrics

        val edgeSlop = edgeSlop
        val rightSlop = metrics.widthPixels - edgeSlop
        val bottomSlop = metrics.heightPixels - edgeSlop

        val x0 = event.rawX
        val y0 = event.rawY
        val x1 = getRawX(event, 1)
        val y1 = getRawY(event, 1)

        val p0sloppy = (x0 < edgeSlop || y0 < edgeSlop || x0 > rightSlop || y0 > bottomSlop)
        val p1sloppy = (x1 < edgeSlop || y1 < edgeSlop || x1 > rightSlop || y1 > bottomSlop)

        if (p0sloppy && p1sloppy)
            return true
        else if (p0sloppy)
            return true
        else if (p1sloppy)
            return true
        else
            return false
    }
}