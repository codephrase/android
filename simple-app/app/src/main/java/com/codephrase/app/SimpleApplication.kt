package com.codephrase.app

import com.codephrase.android.app.Application
import com.codephrase.android.common.notification.NotificationChannel
import com.codephrase.android.common.notification.NotificationGroup

class SimpleApplication : Application() {
    private val notificationGroups: List<NotificationGroup> by lazy {
        arrayListOf(
                createGroup01(),
                createGroup02()
        )
    }

    private val notificationChannels: List<NotificationChannel> by lazy {
        arrayListOf(
                createChannel01(),
                createChannel02(),
                createChannel03()
        )
    }

    override val multiDexEnabled: Boolean
        get() = true

    override fun onCreateNotificationGroups(): List<NotificationGroup> {
        return notificationGroups
    }

    override fun onCreateNotificationChannels(): List<NotificationChannel> {
        return notificationChannels
    }

    private fun createGroup01(): NotificationGroup {
        return NotificationGroup(AppConstants.NOTIFICATION_GROUP_01, "Group 01")
    }

    private fun createGroup02(): NotificationGroup {
        return NotificationGroup(AppConstants.NOTIFICATION_GROUP_02, "Group 02")
    }

    private fun createChannel01(): NotificationChannel {
        val channel = NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_01, "Channel 01")
        channel.groupId = AppConstants.NOTIFICATION_GROUP_01
        return channel
    }

    private fun createChannel02(): NotificationChannel {
        val channel = NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_02, "Channel 02")
        channel.groupId = AppConstants.NOTIFICATION_GROUP_01
        return channel
    }

    private fun createChannel03(): NotificationChannel {
        val channel = NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_03, "Channel 03")
        channel.groupId = AppConstants.NOTIFICATION_GROUP_02
        return channel
    }
}