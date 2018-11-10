package com.codephrase.android.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codephrase.android.common.recyclerview.RecyclerViewAdapter

@BindingAdapter("items")
fun RecyclerView.setItems(oldItems: List<Any>?, newItems: List<Any>?) {
    if (oldItems != newItems) {
        val adapter = adapter as RecyclerViewAdapter?
        adapter?.let {
            it.setItems(newItems)
            it.notifyDataSetChanged()
        }
    }
}