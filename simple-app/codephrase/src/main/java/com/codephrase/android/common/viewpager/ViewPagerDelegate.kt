package com.codephrase.android.common.viewpager

import androidx.fragment.app.Fragment

interface ViewPagerDelegate {
    fun getItemCount(): Int

    fun getItemTitle(position: Int): CharSequence?

    fun getItemView(position: Int): Fragment
}