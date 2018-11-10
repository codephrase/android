package com.codephrase.android.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codephrase.android.common.recyclerview.OnItemClickListener
import com.codephrase.android.common.recyclerview.RecyclerViewAdapter
import com.codephrase.android.fragment.FrameFragment
import kotlin.reflect.KClass

abstract class RecyclerActivity : FrameActivity() {
    final override val contentFragmentType: KClass<out FrameFragment>?
        get() = super.contentFragmentType

    protected open val recyclerViewId: Int
        get() = 0

    protected var recyclerView: RecyclerView? = null

    protected var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        if (recyclerViewId > 0) {
            recyclerView = findViewById(recyclerViewId)
            recyclerView?.let {
                it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                val onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(itemView: View, item: Any?) {
                        return this@RecyclerActivity.onItemClick(itemView, item)
                    }
                }

                recyclerViewAdapter = object : RecyclerViewAdapter(onItemClickListener) {
                    override fun getItemCount(): Int {
                        return this@RecyclerActivity.getItemCount()
                    }

                    override fun getItemObject(position: Int): Any? {
                        return this@RecyclerActivity.getItemObject(position)
                    }

                    override fun getItemViewType(position: Int): Int {
                        return this@RecyclerActivity.getItemViewType(position)
                    }

                    override fun getItemLayoutId(viewType: Int): Int {
                        return this@RecyclerActivity.getItemLayoutId(viewType)
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