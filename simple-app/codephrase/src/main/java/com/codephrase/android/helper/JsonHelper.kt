package com.codephrase.android.helper

import com.google.gson.Gson
import kotlin.reflect.KClass

class JsonHelper private constructor() {
    companion object {
        fun serialize(obj: Any?): String {
            return Gson().toJson(obj)
        }

        fun <T : Any> deserialize(str: String, type: KClass<T>): T? {
            return Gson().fromJson(str, type.java)
        }
    }
}