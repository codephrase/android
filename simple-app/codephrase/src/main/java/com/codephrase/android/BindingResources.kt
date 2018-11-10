package com.codephrase.android

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.codephrase.android.viewmodel.ViewModel

private class BindingResources() : BaseObservable() {
    val viewModel: ViewModel?
        @Bindable get() {
            return null
        }

    val item: Any?
        @Bindable get() {
            return null
        }
}