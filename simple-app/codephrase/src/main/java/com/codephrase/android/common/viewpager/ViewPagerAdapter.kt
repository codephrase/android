package com.codephrase.android.common.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class ViewPagerAdapter : FragmentPagerAdapter {
    private var items: List<Any>? = null
    private var delegate: ViewPagerDelegate?

    constructor(manager: FragmentManager) : super(manager) {
        this.delegate = null
    }

    constructor(manager: FragmentManager, delegate: ViewPagerDelegate) : super(manager) {
        this.delegate = delegate
    }

    fun getItems(): List<Any>? {
        return items
    }

    fun setItems(items: List<Any>?) {
        this.items = items
    }

    override fun getCount(): Int {
        return delegate?.let { it.getItemCount() } ?: run { 0 }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return delegate?.let { it.getItemTitle(position) } ?: run { super.getPageTitle(position) }
    }

    override fun getItem(position: Int): Fragment {
        return delegate?.let { it.getItemView(position) } ?: run { Fragment() }
    }
}