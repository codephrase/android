package com.codephrase.android.common.notification

class NotificationId {
    private val id: Int

    internal constructor(id: Int) {
        this.id = id
    }

    internal fun get(): Int {
        return id
    }

    override fun toString(): String {
        return id.toString()
    }
}