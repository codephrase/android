package com.codephrase.android.helper

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.codephrase.android.common.notification.*
import com.codephrase.android.constant.NotificationConstants
import com.codephrase.android.exception.NotFoundException
import android.app.NotificationChannel as AndroidNotificationChannel
import android.app.NotificationChannelGroup as AndroidNotificationGroup

class NotificationHelper private constructor() {
    companion object {
        fun initialize() {
            val groups = ApplicationHelper.getNotificationGroups()
            val channels = ApplicationHelper.getNotificationChannels()

            val groupIds = groups.map { it.id }

            channels.firstOrNull {
                val groupId = it.groupId
                !groupId.isNullOrEmpty() && !groupIds.contains(groupId)
            }?.apply {
                throw NotFoundException("Notification group (${this.groupId})")
            }

            if (ApplicationHelper.isApiLevel26Supported()) {
                val manager = ApplicationHelper.getContext().getSystemService(NotificationManager::class.java)
                setupNotificationChannels(manager, groups, channels)
            }
        }

        fun notify(notification: NotificationData): NotificationId {
            val context = ApplicationHelper.getContext()
            return notify(context, generateNotificationId(context), notification)
        }

        fun notify(id: NotificationId, notification: NotificationData): NotificationId {
            return notify(ApplicationHelper.getContext(), id, notification)
        }

        fun notify(context: Context, id: NotificationId, notification: NotificationData): NotificationId {
            val channel = resolveNotificationChannel(notification.channelId)

            channel?.let {
                val builder = NotificationCompat.Builder(context, it.id)

                notification.smallIcon.takeIf { it > 0 }
                        ?.apply {
                            builder.setSmallIcon(this)
                        }

                notification.title?.takeIf { it.isNotEmpty() }
                        ?.apply {
                            builder.setContentTitle(this)
                        }

                notification.content?.takeIf { it.isNotEmpty() }
                        ?.apply {
                            builder.setContentText(this)
                        }

                notification.timestamp.takeIf { it > 0 }
                        ?.apply {
                            builder.setWhen(this)
                        }

                builder.priority = when (it.priority) {
                    NotificationPriority.URGENT -> NotificationCompat.PRIORITY_HIGH
                    NotificationPriority.HIGH -> NotificationCompat.PRIORITY_DEFAULT
                    NotificationPriority.MEDIUM -> NotificationCompat.PRIORITY_LOW
                    NotificationPriority.LOW -> NotificationCompat.PRIORITY_MIN
                }

                builder.setVisibility(when (it.visibility) {
                    NotificationVisibility.PUBLIC -> Notification.VISIBILITY_PUBLIC
                    NotificationVisibility.PRIVATE -> Notification.VISIBILITY_PRIVATE
                    NotificationVisibility.SECRET -> Notification.VISIBILITY_SECRET
                })

                it.sound?.let { builder.setSound(it) }

                if (it.lightEnabled)
                    builder.setLights(it.lightColor, 500, 2000)

                if (it.vibrationEnabled)
                    builder.setVibrate(it.vibrationPattern)

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(id.get(), builder.build())
                return id
            } ?: run {
                throw NotFoundException("Notification channel (${notification.channelId})")
            }
        }

        fun cancel(id: NotificationId) {
            val notificationManager = NotificationManagerCompat.from(ApplicationHelper.getContext())
            notificationManager.cancel(id.get())
        }

        fun cancelAll() {
            val notificationManager = NotificationManagerCompat.from(ApplicationHelper.getContext())
            notificationManager.cancelAll()
        }

        private fun generateNotificationId(context: Context): NotificationId {
            val pref = StorageHelper.getSharedPreference(context.packageName)
            var id = pref.getInt(NotificationConstants.NOTIFICATION_ID, 0) % Int.MAX_VALUE + 1

            pref.putInt(NotificationConstants.NOTIFICATION_ID, id)
            return NotificationId(id)
        }

        private fun resolveNotificationChannel(id: String): NotificationChannel? {
            val channels = ApplicationHelper.getNotificationChannels()
            return channels.firstOrNull { it.id == id }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun setupNotificationChannels(manager: NotificationManager, groups: List<NotificationGroup>, channels: List<NotificationChannel>) {
            if (groups.isNotEmpty())
                manager.createNotificationChannelGroups(generateAndroidNotificationGroups(groups))

            if (channels.isNotEmpty())
                manager.createNotificationChannels(generateAndroidNotificationChannels(channels))
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun generateAndroidNotificationGroups(groups: List<NotificationGroup>): List<AndroidNotificationGroup> {
            return groups.map { AndroidNotificationGroup(it.id, it.name) }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun generateAndroidNotificationChannels(channels: List<NotificationChannel>): List<AndroidNotificationChannel> {
            return channels.map {
                val notificationChannel = AndroidNotificationChannel(it.id, it.name, when (it.priority) {
                    NotificationPriority.URGENT -> NotificationManager.IMPORTANCE_HIGH
                    NotificationPriority.HIGH -> NotificationManager.IMPORTANCE_DEFAULT
                    NotificationPriority.MEDIUM -> NotificationManager.IMPORTANCE_LOW
                    NotificationPriority.LOW -> NotificationManager.IMPORTANCE_MIN
                })
                notificationChannel.description = it.description
                notificationChannel.lockscreenVisibility = when (it.visibility) {
                    NotificationVisibility.PUBLIC -> Notification.VISIBILITY_PUBLIC
                    NotificationVisibility.PRIVATE -> Notification.VISIBILITY_PRIVATE
                    NotificationVisibility.SECRET -> Notification.VISIBILITY_SECRET
                }
                notificationChannel.setSound(it.sound, Notification.AUDIO_ATTRIBUTES_DEFAULT)
                notificationChannel.enableLights(it.lightEnabled)
                notificationChannel.lightColor = it.lightColor
                notificationChannel.enableVibration(it.vibrationEnabled)
                notificationChannel.vibrationPattern = it.vibrationPattern
                notificationChannel.setShowBadge(it.badgeEnabled)
                notificationChannel.group = it.groupId
                notificationChannel
            }
        }
    }
}