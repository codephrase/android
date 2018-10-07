package com.codephrase.android.common.notification

class NotificationData {
    val channelId: String
    var smallIcon: Int = 0
    var title: String? = null
    var content: String? = null
    var timestamp: Long = 0

    constructor(channelId: String) {
        this.channelId = channelId
    }
}