package com.codephrase.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.codephrase.android.gesture.MoveGestureDetector

open class ZoomImageView : View {
    private var imageDrawable: Drawable? = null

    private val commonGestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            performClick()
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            performLongClick()
        }
    })

    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5f))
            return true
        }
    })

    private val moveGestureDetector: MoveGestureDetector = MoveGestureDetector(context, object : MoveGestureDetector.SimpleOnMoveGestureListener() {
        override fun onMove(detector: MoveGestureDetector): Boolean {
            val d = detector.focusDelta
            posX += d.x
            posY += d.y
            return true
        }
    })

    private var scaleFactor = 1f
    private var posX = 0f
    private var posY = 0f

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    fun setImageDrawable(imageDrawable: Drawable?) {
        this.imageDrawable = imageDrawable

        invalidateImageBound()
        requestLayout()
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            imageDrawable?.let { drawable ->
                val saveCount = it.save()
                it.translate(paddingLeft.toFloat(), paddingTop.toFloat())
                drawable.draw(canvas)
                it.restoreToCount(saveCount)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        commonGestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        moveGestureDetector.onTouchEvent(event)

        invalidateImageBound()
        invalidate()

        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var dw = 0
        var dh = 0

        imageDrawable?.let {
            dw = it.intrinsicWidth
            dh = it.intrinsicHeight
        }

        dw += paddingLeft + paddingRight
        dh += paddingTop + paddingBottom

        val measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0)
        val measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        invalidateImageBound(w, h)
    }

    private fun invalidateImageBound() {
        invalidateImageBound(measuredWidth, measuredHeight)
    }

    private fun invalidateImageBound(width: Int, height: Int) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        var w = width - (paddingRight + paddingLeft)
        var h = height - (paddingTop + paddingBottom)

        var right = w
        var bottom = h
        var top = 0
        var left = 0

        imageDrawable?.let {
            // Maintain aspect ratio. Certain kinds of animated drawables
            // get very confused otherwise.
            val intrinsicWidth = it.intrinsicWidth
            val intrinsicHeight = it.intrinsicHeight

            val intrinsicAspect = intrinsicWidth.toFloat() / intrinsicHeight
            val boundAspect = w.toFloat() / h

            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    // New width is larger. Make it smaller to match height.
                    val width = (h * intrinsicAspect).toInt()
                    left = (w - width) / 2
                    right = left + width
                } else {
                    // New height is larger. Make it smaller to match width.
                    val height = (w * (1 / intrinsicAspect)).toInt()
                    top = (h - height) / 2
                    bottom = top + height
                }
            }

            if (scaleFactor > 1) {
                val maxW = w.toFloat()
                val maxH = h.toFloat()

                val realW = (right - left).toFloat()
                val realH = (bottom - top).toFloat()

                val scaledW = realW * scaleFactor
                val scaledH = realH * scaleFactor

                val offscreenW = (maxW - realW) / 2
                val offscreenH = (maxH - realH) / 2

                val diffW = (scaledW - realW) / 2
                val diffH = (scaledH - realH) / 2

                left -= diffW.toInt()
                right += diffW.toInt()
                top -= diffH.toInt()
                bottom += diffH.toInt()

                if (scaledW > maxW) {
                    if (posX > diffW - offscreenW)
                        posX = diffW - offscreenW
                    else if (posX < -diffW + offscreenW)
                        posX = -diffW + offscreenW
                } else {
                    posX = 0f
                }

                if (scaledH > maxH) {
                    if (posY > diffH - offscreenH)
                        posY = diffH - offscreenH
                    else if (posY < -diffH + offscreenH)
                        posY = -diffH + offscreenH
                } else {
                    posY = 0f
                }

                left += posX.toInt()
                right += posX.toInt()
                top += posY.toInt()
                bottom += posY.toInt()
            } else {
                posX = 0f
                posY = 0f
            }

            it.setBounds(left, top, right, bottom)
        }
    }
}