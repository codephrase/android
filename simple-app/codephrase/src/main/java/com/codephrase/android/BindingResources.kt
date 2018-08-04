package com.codephrase.android

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.codephrase.android.viewmodel.ViewModel

private class BindingResources() : BaseObservable() {
    val viewModel: ViewModel?
        @Bindable get() {
            return null
        }
}