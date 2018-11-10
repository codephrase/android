package com.codephrase.android.common.recyclerview

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.codephrase.android.BR

open class RecyclerViewHolder : RecyclerView.ViewHolder {
    private val binding: ViewDataBinding?

    constructor(itemView: View) : super(itemView) {
        binding = DataBindingUtil.bind(itemView)
    }

    fun bind(item: Any?) {
        bind(item, null)
    }

    fun bind(item: Any?, onItemClickListener: OnItemClickListener?) {
        binding?.let {
            it.setVariable(BR.item, item)
            it.executePendingBindings()
        }

        itemView.setOnClickListener { view ->
            onItemClickListener?.let {
                it.onItemClick(view, item)
            }
        }

        onItemClickListener?.let { listener ->
            itemView.setOnClickListener {
                listener.onItemClick(it, item)
            }
        }
    }
}