package com.codephrase.android.common

import android.content.Context
import com.codephrase.android.helper.SecurityHelper

class SecurePreference(context: Context, name: String) {
    private val pref by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun contains(key: String): Boolean {
        return pref.contains(hashKey(key))
    }

    fun remove(key: String) {
        val editor = pref.edit()
        editor.remove(hashKey(key))
        editor.apply()
    }

    fun getString(key: String, defValue: String?): String? {
        val hashedKey = hashKey(key)

        if (pref.contains(hashedKey))
            return decryptValue(pref.getString(hashedKey, null))
        else
            return defValue
    }

    fun putString(key: String, value: String?) {
        value?.let {
            val editor = pref.edit()
            editor.putString(hashKey(key), encryptValue(it))
            editor.apply()
        } ?: run {
            remove(key)
        }
    }

    fun getStringSet(key: String, defValue: Set<String>?): Set<String>? {
        val hashedKey = hashKey(key)

        if (pref.contains(hashedKey))
            return decryptValue(pref.getStringSet(hashedKey, null))
        else
            return defValue
    }

    fun putStringSet(key: String, value: Set<String>?) {
        value?.let {
            val editor = pref.edit()
            editor.putStringSet(hashKey(key), encryptValue(value))
            editor.apply()
        } ?: run {
            remove(key)
        }
    }

    fun getInt(key: String, defValue: Int): Int {
        val hashedKey = hashKey(key)

        if (pref.contains(hashedKey))
            return decryptValue(pref.getString(hashedKey, null)).toInt()
        else
            return defValue
    }

    fun putInt(key: String, value: Int) {
        val editor = pref.edit()
        editor.putString(hashKey(key), encryptValue(value.toString()))
        editor.apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        val hashedKey = hashKey(key)

        if (pref.contains(hashedKey))
            return decryptValue(pref.getString(hashedKey, null)).toLong()
        else
            return defValue
    }

    fun putLong(key: String, value: Long) {
        val editor = pref.edit()
        editor.putString(hashKey(key), encryptValue(value.toString()))
        editor.apply()
    }

    fun getFloat(key: String, defValue: Float): Float {
        val hashedKey = hashKey(key)

        if (pref.contains(hashedKey))
            return decryptValue(pref.getString(hashedKey, null)).toFloat()
        else
            return defValue
    }

    fun putFloat(key: String, value: Float) {
        val editor = pref.edit()
        editor.putString(hashKey(key), encryptValue(value.toString()))
        editor.apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val hashedKey = hashKey(key)

        if (pref.contains(hashedKey))
            return decryptValue(pref.getString(hashedKey, null)).toBoolean()
        else
            return defValue
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putString(hashKey(key), encryptValue(value.toString()))
        editor.apply()
    }

    private fun hashKey(key: String): String {
        return SecurityHelper.hash(key)
    }

    private fun encryptValue(value: String): String {
        return SecurityHelper.encrypt(value)
    }

    private fun decryptValue(value: String): String {
        return SecurityHelper.decrypt(value)
    }

    private fun encryptValue(value: Set<String>?): Set<String>? {
        value?.let {
            val encryptedValue = HashSet<String>()
            it.forEach {
                encryptedValue.add(encryptValue(it))
            }
            return encryptedValue
        } ?: run {
            return null
        }
    }

    private fun decryptValue(value: Set<String>?): Set<String>? {
        value?.let {
            val decryptedValue = HashSet<String>()
            it.forEach {
                decryptedValue.add(decryptValue(it))
            }
            return decryptedValue
        } ?: run {
            return null
        }
    }
}