package com.codephrase.android.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.codephrase.android.common.viewpager.ViewPagerAdapter

@BindingAdapter("items")
fun ViewPager.setItems(oldItems: List<Any>?, newItems: List<Any>?) {
    if (oldItems != newItems) {
        val adapter = adapter as ViewPagerAdapter?
        adapter?.let {
            it.setItems(newItems)
            it.notifyDataSetChanged()
        }
    }
}