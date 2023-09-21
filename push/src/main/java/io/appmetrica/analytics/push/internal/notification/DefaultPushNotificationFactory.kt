package io.appmetrica.analytics.push.internal.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.Constants
import io.appmetrica.analytics.push.impl.NotificationCustomizersHolderProvider
import io.appmetrica.analytics.push.impl.notification.DefaultNotificationCustomizersHolder
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.notification.NotificationCustomizersHolder
import io.appmetrica.analytics.push.settings.PushNotificationFactory

class DefaultPushNotificationFactory : PushNotificationFactory {

    override fun buildNotification(
        context: Context,
        pushMessage: PushMessage
    ): Notification? {
        return if (isValid(pushMessage)) {
            NotificationCompat.Builder(context)
                .fillNotificationBuilder(context, pushMessage)
                .build()
        } else {
            reportInvalidPush(pushMessage)
            null
        }
    }

    private fun isValid(pushMessage: PushMessage): Boolean {
        val contentTitle = pushMessage.notification?.contentTitle
        val contentText = pushMessage.notification?.contentText
        return CoreUtils.isNotEmpty(contentTitle) || CoreUtils.isNotEmpty(contentText)
    }

    private fun reportInvalidPush(pushMessage: PushMessage) {
        val pushId = pushMessage.notificationId
        PublicLogger.i("Push filtered out. PushMessage does not contain content title and content text")
        if (CoreUtils.isNotEmpty(pushId)) {
            PushMessageTrackerHub.getInstance().onNotificationIgnored(
                pushId!!,
                Constants.IGNORED_CATEGORY_PUSH_DATA_FORMAT_IS_INVALID,
                "Not all required fields were set",
                pushMessage.payload,
                pushMessage.transport
            )
        }
    }

    private fun NotificationCompat.Builder.fillNotificationBuilder(
        context: Context,
        pushMessage: PushMessage
    ): NotificationCompat.Builder = apply {
        val defaultCustomizer = DefaultNotificationCustomizersHolder(context)
        val userCustomizer = NotificationCustomizersHolderProvider.customizersHolder
        reportUserCustomizer(userCustomizer, pushMessage)
        val providers = defaultCustomizer.getCustomizers() + userCustomizer.customizers
        userCustomizer.beforeCustomizer?.invoke(this, pushMessage)
        providers.forEach {
            it.value.invoke(this, pushMessage)
        }
        userCustomizer.afterCustomizer?.invoke(this, pushMessage)
    }

    private fun reportUserCustomizer(
        userCustomizer: NotificationCustomizersHolder,
        pushMessage: PushMessage
    ) {
        val beforeCustomizer = userCustomizer.beforeCustomizer
        val customizers = userCustomizer.customizers
        val afterCustomizer = userCustomizer.afterCustomizer

        if (beforeCustomizer != null || customizers.isNotEmpty() || afterCustomizer != null) {
            TrackersHub.getInstance().reportEvent(
                "DefaultPushNotificationFactory.buildNotification",
                mapOf(
                    "pushId" to pushMessage.notificationId,
                    "useBeforeCustomizer" to (beforeCustomizer != null),
                    "useCustomizers" to customizers.size,
                    "useAfterCustomizer" to (afterCustomizer != null)
                )
            )
        }
    }
}
