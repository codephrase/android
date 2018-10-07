package com.codephrase.android.common.notification

import android.graphics.Color
import android.net.Uri

class NotificationChannel {
    val id: String
    val name: String
    var description: String?
    var priority: NotificationPriority
    var visibility: NotificationVisibility
    var sound: Uri?
    var lightEnabled: Boolean
    var lightColor: Int
    var vibrationEnabled: Boolean
    var vibrationPattern: LongArray
    var badgeEnabled: Boolean
    var groupId: String?

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
        this.description = null
        this.priority = NotificationPriority.HIGH
        this.visibility = NotificationVisibility.PUBLIC
        this.sound = null
        this.lightEnabled = false
        this.lightColor = Color.WHITE
        this.vibrationEnabled = false
        this.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        this.badgeEnabled = true
        this.groupId = null
    }
}