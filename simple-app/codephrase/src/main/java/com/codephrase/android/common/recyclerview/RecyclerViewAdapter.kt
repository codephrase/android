package com.codephrase.android.common.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewHolder> {
    private var items: List<Any>? = null
    private val onItemClickListener: OnItemClickListener?

    constructor() : super() {
        this.onItemClickListener = null
    }

    constructor(onItemClickListener: OnItemClickListener) : super() {
        this.onItemClickListener = onItemClickListener
    }

    fun getItems(): List<Any>? {
        return items
    }

    fun setItems(items: List<Any>?) {
        this.items = items
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    open fun getItemObject(position: Int): Any? {
        return items?.get(position)
    }

    open fun getItemLayoutId(viewType: Int): Int {
        return 0
    }

    open fun getItemView(parent: ViewGroup, viewType: Int): View {
        val itemLayoutId = getItemLayoutId(viewType)
        return LayoutInflater.from(parent.context)
                .inflate(itemLayoutId, parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = getItemView(parent, viewType)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(getItemObject(position), onItemClickListener)
    }
}