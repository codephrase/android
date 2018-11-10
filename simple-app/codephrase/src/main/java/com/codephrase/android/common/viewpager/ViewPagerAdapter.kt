package com.codephrase.android.common.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.codephrase.android.constant.NavigationConstants
import com.codephrase.android.helper.JsonHelper

abstract class ViewPagerAdapter : FragmentPagerAdapter {
    private var items: List<Any>? = null

    constructor(manager: FragmentManager) : super(manager) {

    }

    fun getItems(): List<Any>? {
        return items
    }

    fun setItems(items: List<Any>?) {
        this.items = items
    }

    override fun getCount(): Int {
        return items?.size ?: 0
    }

    open fun getItemObject(position: Int): Any? {
        return items?.get(position)
    }

    open fun getItemView(position: Int): Fragment {
        return Fragment()
    }

    final override fun getItem(position: Int): Fragment {
        val fragment = getItemView(position)

        val item = getItemObject(position)
        item?.let {
            val arguments = fragment.arguments ?: Bundle()
            arguments.putSerializable(NavigationConstants.DATA_TYPE, it.javaClass)
            arguments.putString(NavigationConstants.DATA_OBJECT, JsonHelper.serialize(it))
            fragment.arguments = arguments
        }

        return fragment
    }
}