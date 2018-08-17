package com.codephrase.android.helper

import com.codephrase.android.common.SecurePreference
import com.codephrase.android.common.SharedPreference

class StorageHelper private constructor() {
    companion object {
        fun getSharedPreference(name: String): SharedPreference {
            return SharedPreference(ApplicationHelper.getContext(), name)
        }

        fun getSecurePreference(name: String): SecurePreference {
            return SecurePreference(ApplicationHelper.getContext(), name)
        }
    }
}