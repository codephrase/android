package com.codephrase.app.activity

import android.os.Bundle
import android.widget.Button
import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.common.notification.NotificationData
import com.codephrase.android.helper.NotificationHelper
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.app.AppConstants
import com.codephrase.app.R
import com.codephrase.app.viewmodel.NotificationViewModel
import kotlin.reflect.KClass

class NotificationActivity : FrameActivity() {
    override val toolbarEnabled: Boolean
        get() = true

    override val contentLayoutId: Int
        get() = R.layout.content_notification

    override val viewModelType: KClass<out ViewModel>
        get() = NotificationViewModel::class

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        val notifyButton = findViewById<Button>(R.id.notify_button)
        notifyButton.setOnClickListener {
            val notification = NotificationData(AppConstants.NOTIFICATION_CHANNEL_01)
            notification.smallIcon = R.drawable.ic_menu_send
            notification.title = "Hello World"
            notification.content = "Lorem ipsum dolor sit amet"
            NotificationHelper.notify(notification)
        }

        val cancelAllButton = findViewById<Button>(R.id.cancel_all_button)
        cancelAllButton.setOnClickListener {
            NotificationHelper.cancelAll()
        }
    }
}