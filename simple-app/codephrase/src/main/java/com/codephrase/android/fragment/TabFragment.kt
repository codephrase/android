package com.codephrase.android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.codephrase.android.R
import com.codephrase.android.common.viewpager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

abstract class TabFragment : FrameFragment() {
    override val layoutId: Int
        get() {
            if (toolbarEnabled) {
                if (headerLayoutId > 0)
                    return R.layout.fragment_tab_collapsing
                else
                    return R.layout.fragment_tab_toolbar
            } else {
                return R.layout.fragment_tab_default
            }
        }

    protected open val tabId: Int
        get() = R.id.tab

    protected open val viewPagerId: Int
        get() = 0

    protected var tab: TabLayout? = null
    protected var viewPager: ViewPager? = null

    protected var viewPagerAdapter: ViewPagerAdapter? = null

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        if (tabId > 0) {
            tab = findViewById(tabId)
            tab?.let {
                it.tabMode = TabLayout.MODE_FIXED
                it.tabGravity = TabLayout.GRAVITY_FILL
            }
        }

        if (viewPagerId > 0) {
            viewPager = findViewById(viewPagerId)
            viewPager?.let {
                fragmentManager?.let { fragmentManager ->
                    viewPagerAdapter = object : ViewPagerAdapter(fragmentManager) {
                        override fun getCount(): Int {
                            return this@TabFragment.getItemCount()
                        }

                        override fun getItemObject(position: Int): Any? {
                            return this@TabFragment.getItemObject(position)
                        }

                        override fun getPageTitle(position: Int): CharSequence? {
                            return this@TabFragment.getItemTitle(position)
                        }

                        override fun getItemView(position: Int): Fragment {
                            return this@TabFragment.getItemView(position)
                        }
                    }
                    it.adapter = viewPagerAdapter
                }

                tab?.setupWithViewPager(it)
            }
        }
    }

    protected open fun getItemCount(): Int {
        return 0
    }

    protected open fun getItemObject(position: Int): Any? {
        return null
    }

    protected open fun getItemTitle(position: Int): CharSequence? {
        return null
    }

    protected open fun getItemView(position: Int): Fragment {
        return Fragment()
    }
}