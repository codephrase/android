package com.codephrase.android.helper

import com.google.gson.Gson
import kotlin.reflect.KClass

class JsonHelper private constructor() {

    companion object {
        fun serialize(obj: Any): String {
            val gson = Gson()
            return gson.toJson(obj)
        }

        fun <T : Any> deserialize(str: String, type: KClass<T>): T {
            val gson = Gson()
            return gson.fromJson(str, type.java);
        }
    }
}