package com.codephrase.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.codephrase.android.helper.ResourceHelper
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.R

class DataBindingViewModel : ViewModel() {
    val content = MutableLiveData<String>()

    init {
        title.value = "Data Binding"
    }

    override fun onDataLoading() {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {

        }

        content.postValue(ResourceHelper.getString(R.string.large_text))

        onDataLoaded()
    }
}