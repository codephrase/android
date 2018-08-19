package com.codephrase.android.extension

import android.view.View
import kotlin.reflect.KClass

fun View.setWidth(width: Int) {
    layoutParams?.let {
        it.width = width
        layoutParams = it
    }
}

fun View.setHeight(height: Int) {
    layoutParams?.let {
        it.height = height
        layoutParams = it
    }
}

fun View.setSize(width: Int, height: Int) {
    layoutParams?.let {
        it.width = width
        it.height = height
        layoutParams = it
    }
}

fun View.setPaddingLeft(paddingLeft: Int) = setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingTop(paddingTop: Int) = setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingRight(paddingRight: Int) = setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingBottom(paddingBottom: Int) = setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingHorizontal(paddingHorizontal: Int) = setPadding(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom)

fun View.setPaddingVertical(paddingVertical: Int) = setPadding(paddingLeft, paddingVertical, paddingRight, paddingVertical)

tailrec fun <T : View> View.findParent(type: KClass<T>): T {
    return if (parent::class == type) parent as T else (parent as View).findParent(type)
}


