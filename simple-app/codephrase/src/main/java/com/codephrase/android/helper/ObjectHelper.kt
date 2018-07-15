package com.codephrase.android.helper

import kotlin.reflect.KClass

class ObjectHelper private constructor() {

    companion object {
        fun <T : Any> create(type: KClass<T>): T? {
            try {
                return type.java.newInstance();
            } catch (e: Exception) {
                return null
            }
        }
    }
}