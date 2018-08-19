package com.codephrase.android.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.os.Looper

abstract class ViewModel : ViewModel() {
    val title = MutableLiveData<String>()
    val dataLoading = MutableLiveData<Boolean>()
    internal val dataLoaded = MutableLiveData<Boolean>()

    internal fun onNavigatedInternal(data: Any?) {
        onNavigated(data)
    }

    protected open fun onNavigated(data: Any?) {

    }

    internal fun loadData() {
        if (canLoadData()) {
            dataLoading.value = true

            runOnBackgroundThread {
                onDataLoading()
            }
        }
    }

    protected open fun canLoadData(): Boolean {
        return dataLoading.value != true
    }

    protected open fun onDataLoading() {
        onDataLoaded()
    }

    protected fun onDataLoaded() {
        runOnUIThread {
            dataLoaded.value = true
            dataLoading.value = false
        }
    }

    private fun runOnBackgroundThread(action: () -> Unit) {
        val thread = Thread(action)
        thread.start()
    }

    private fun runOnUIThread(action: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.post(action)
    }
}