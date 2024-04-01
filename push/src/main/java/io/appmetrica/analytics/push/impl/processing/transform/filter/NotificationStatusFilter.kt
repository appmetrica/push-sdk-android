package io.appmetrica.analytics.push.impl.processing.transform.filter

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationManagerCompat
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController
import io.appmetrica.analytics.push.impl.utils.ChannelHelper
import io.appmetrica.analytics.push.impl.utils.ChannelPostPHelper
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.settings.PushFilter

internal class NotificationStatusFilter
@VisibleForTesting
constructor(
    private val notificationManager: NotificationManager,
    private val notificationManagerCompat: NotificationManagerCompat
) : PushFilter {
    constructor(context: Context) : this(
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
        NotificationManagerCompat.from(context)
    )

    private val tag = "[NotificationStatusFilter]"

    private val disabledSystemNotification = "Disabled system notification"
    private val disabledAllNotifications = "Disabled all notifications"

    override fun filter(pushMessage: PushMessage): PushFilter.FilterResult {
        if (pushMessage.isSilent) {
            PLog.i("[$tag] Show silent push")
            return PushFilter.FilterResult.show()
        }
        if (!notificationManagerCompat.areNotificationsEnabled()) {
            return PushFilter.FilterResult.silence(disabledSystemNotification, disabledAllNotifications)
        }

        return checkNotificationChannelState(pushMessage)
    }

    private fun checkNotificationChannelState(pushMessage: PushMessage): PushFilter.FilterResult {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = pushMessage.notification?.channelId?.takeIf { it.isNotBlank() }
                ?: NotificationChannelController.DEFAULT_CHANNEL_ID
            val importance = ChannelHelper.getNotificationChannelImportance(notificationManager, channelId)

            if (importance == NotificationManager.IMPORTANCE_NONE) {
                return PushFilter.FilterResult.silence(
                    disabledSystemNotification,
                    String.format("Disabled notifications for \"%s\" channel", channelId)
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (ChannelPostPHelper.isNotificationChannelGroupBlocked(notificationManager, channelId)) {
                    val groupId = ChannelHelper.getNotificationChannelGroupId(notificationManager, channelId)
                    return PushFilter.FilterResult.silence(
                        disabledSystemNotification,
                        String.format("Disabled notifications for \"%s\" group", groupId)
                    )
                }
            }
        }

        return PushFilter.FilterResult.show()
    }
}
