package com.codephrase.android.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.codephrase.android.R
import com.codephrase.android.common.viewpager.ViewPagerAdapter
import com.codephrase.android.fragment.FrameFragment
import com.google.android.material.tabs.TabLayout
import kotlin.reflect.KClass

abstract class TabActivity : FrameActivity() {
    override val layoutId: Int
        get() {
            if (toolbarEnabled) {
                if (headerLayoutId > 0)
                    return R.layout.activity_tab_collapsing
                else
                    return R.layout.activity_tab_toolbar
            } else {
                return R.layout.activity_tab_default
            }
        }

    final override val contentFragmentType: KClass<out FrameFragment>?
        get() = super.contentFragmentType

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
                viewPagerAdapter = object : ViewPagerAdapter(supportFragmentManager) {
                    override fun getCount(): Int {
                        return this@TabActivity.getItemCount()
                    }

                    override fun getItemObject(position: Int): Any? {
                        return this@TabActivity.getItemObject(position)
                    }

                    override fun getPageTitle(position: Int): CharSequence? {
                        return this@TabActivity.getItemTitle(position)
                    }

                    override fun getItemView(position: Int): Fragment {
                        return this@TabActivity.getItemView(position)
                    }
                }
                it.adapter = viewPagerAdapter

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