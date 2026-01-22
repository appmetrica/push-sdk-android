package io.appmetrica.analytics.push.impl.processing

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.Constants
import io.appmetrica.analytics.push.impl.PushNotificationFactoryProvider
import io.appmetrica.analytics.push.impl.notification.PushIdFinder
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.impl.utils.ChannelHelper
import io.appmetrica.analytics.push.logger.internal.DebugLogger
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.settings.PushNotificationFactory

internal class NotificationPublisher {

    private val tag = "[NotificationPublisher]"

    fun publishNotification(
        context: Context,
        notificationFactory: PushNotificationFactory,
        pushMessage: PushMessage
    ) {
        val notification = notificationFactory.buildNotification(context, pushMessage)

        val notificationId = pushMessage.notification?.notificationId ?: 0
        val notificationTag = pushMessage.notification?.notificationTag

        reportPublishNotification(notificationFactory, pushMessage)
        if (notification != null) {
            reportReplaceIfNecessary(
                context,
                notificationTag,
                notificationId,
                pushMessage.notificationId,
                pushMessage.transport
            )
            if (isChannelExistsForNotification(context, notification)) {
                publishNotification(context, notification, notificationId, notificationTag)
                if (CoreUtils.isNotEmpty(pushMessage.notificationId)) {
                    PushMessageTrackerHub.getInstance()
                        .onNotificationShown(
                            pushMessage.notificationId!!,
                            pushMessage.payload,
                            pushMessage.transport
                        )
                    AppMetricaPushCore.getInstance(context).pushMessageHistory.also { history ->
                        history.savePushInfo(
                            pushMessage.notificationId!!,
                            notificationId,
                            notificationTag,
                            true
                        )
                        history.savePushTimestamp(pushMessage)
                    }
                }
            } else {
                reportIgnoredSinceChannelIsAbsent(pushMessage, notification)
            }
        } else {
            reportIgnoredSinceNotificationIsNull(pushMessage)
        }
    }

    private fun reportPublishNotification(
        notificationFactory: PushNotificationFactory,
        pushMessage: PushMessage
    ) {

        TrackersHub.getInstance().reportEvent(
            "NotificationPublisher.publishNotification",
            mapOf(
                "pushId" to pushMessage.notificationId,
                "isDefaultNotificationFactory" to PushNotificationFactoryProvider.isDefault(notificationFactory)
            )
        )
    }

    private fun reportReplaceIfNecessary(
        context: Context,
        notificationTag: String?,
        notificationId: Int,
        pushId: String?,
        transport: String
    ) {
        PushIdFinder(context).findActive(notificationTag, notificationId)?.also { replacedPushId ->
            AppMetricaPushCore.getInstance(context)
                .pushServiceProvider
                .pushMessageTracker
                .onNotificationReplace(replacedPushId, pushId, transport)
            AppMetricaPushCore.getInstance(context)
                .pushMessageHistory
                .setPushActive(replacedPushId, false)
        }
    }

    private fun isChannelExistsForNotification(
        context: Context,
        notification: Notification
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChannelHelper.doesChannelExistForNotification(context, notification)
        } else {
            true
        }
    }

    private fun reportIgnoredSinceChannelIsAbsent(
        pushMessage: PushMessage,
        notification: Notification
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChannelHelper.reportIgnoredSinceChannelIsAbsent(pushMessage, notification)
        }
    }

    private fun reportIgnoredSinceNotificationIsNull(
        pushMessage: PushMessage
    ) {
        if (!CoreUtils.isEmpty(pushMessage.notificationId)) {
            PushMessageTrackerHub.getInstance()
                .onNotificationIgnored(
                    pushMessage.notificationId!!,
                    Constants.IGNORED_CATEGORY_NOTIFICATION_IS_NULL,
                    "",
                    pushMessage.payload,
                    pushMessage.transport
                )
        }
    }

    private fun publishNotification(
        context: Context,
        notification: Notification,
        notificationId: Int,
        notificationTag: String?
    ) {
        try {
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                ?.notify(notificationTag, notificationId, notification)
        } catch (th: Throwable) {
            val msg = "Failed show notification with tag $notificationTag and id $notificationId"
            DebugLogger.error(tag, th, msg)
            TrackersHub.getInstance().reportError(msg, th)
        }
    }
}
