package com.codephrase.app.activity

import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.fragment.CollapsingPageFragment
import com.codephrase.app.viewmodel.FragmentPageViewModel
import kotlin.reflect.KClass

class FragmentPageActivity : FrameActivity() {
    override val contentFragmentType: KClass<out FrameFragment>?
        get() = CollapsingPageFragment::class

    override val viewModelType: KClass<out ViewModel>
        get() = FragmentPageViewModel::class
}