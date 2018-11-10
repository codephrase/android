package com.codephrase.android.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codephrase.android.common.recyclerview.OnItemClickListener
import com.codephrase.android.common.recyclerview.RecyclerViewAdapter

abstract class RecyclerFragment : FrameFragment() {
    protected open val recyclerViewId: Int
        get() = 0

    protected var recyclerView: RecyclerView? = null

    protected var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        if (recyclerViewId > 0) {
            recyclerView = findViewById(recyclerViewId)
            recyclerView?.let {
                it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

                val onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(itemView: View, item: Any?) {
                        return this@RecyclerFragment.onItemClick(itemView, item)
                    }
                }

                recyclerViewAdapter = object : RecyclerViewAdapter(onItemClickListener) {
                    override fun getItemCount(): Int {
                        return this@RecyclerFragment.getItemCount()
                    }

                    override fun getItemObject(position: Int): Any? {
                        return this@RecyclerFragment.getItemObject(position)
                    }

                    override fun getItemViewType(position: Int): Int {
                        return this@RecyclerFragment.getItemViewType(position)
                    }

                    override fun getItemLayoutId(viewType: Int): Int {
                        return this@RecyclerFragment.getItemLayoutId(viewType)
                    }
                }
                it.adapter = recyclerViewAdapter
            }
        }
    }

    protected open fun getItemCount(): Int {
        return 0
    }

    protected open fun getItemObject(position: Int): Any? {
        return null
    }

    protected open fun getItemViewType(position: Int): Int {
        return 0
    }

    protected open fun getItemLayoutId(viewType: Int): Int {
        return 0
    }

    protected open fun onItemClick(itemView: View, item: Any?) {

    }
}