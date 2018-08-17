package com.codephrase.android.common

import android.content.Context

class SharedPreference(context: Context, name: String) {
    private val pref by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun remove(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getString(key: String, defValue: String?): String? {
        return pref.getString(key, defValue)
    }

    fun putString(key: String, value: String?) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringSet(key: String, defValue: Set<String>?): Set<String>? {
        return pref.getStringSet(key, defValue)
    }

    fun putStringSet(key: String, value: Set<String>?) {
        val editor = pref.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getFloat(key: String, defValue: Float): Float {
        return pref.getFloat(key, defValue)
    }

    fun putFloat(key: String, value: Float) {
        val editor = pref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}