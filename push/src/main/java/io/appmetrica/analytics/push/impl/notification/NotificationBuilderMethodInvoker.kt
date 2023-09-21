package io.appmetrica.analytics.push.impl.notification

import androidx.core.app.NotificationCompat
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.notification.NotificationValueProvider

object NotificationBuilderMethodInvoker {

    @JvmStatic
    fun <T : Any> invoke(
        method: NotificationCompat.Builder.(T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<T>,
        builder: NotificationCompat.Builder,
        pushMessage: PushMessage
    ) {
        provider.get(pushMessage)?.let {
            method.invoke(builder, it)
        }
    }

    @JvmStatic
    fun <T : Any> invoke(
        method: NotificationCompat.Builder.(T, T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<List<T>>,
        builder: NotificationCompat.Builder,
        pushMessage: PushMessage
    ) {
        provider.get(pushMessage)?.let {
            method.invoke(builder, it[0], it[1])
        }
    }

    @JvmStatic
    fun <T : Any> invoke(
        method: NotificationCompat.Builder.(T, T, T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<List<T>>,
        builder: NotificationCompat.Builder,
        pushMessage: PushMessage
    ) {
        provider.get(pushMessage)?.let {
            method.invoke(builder, it[0], it[1], it[2])
        }
    }

    @JvmStatic
    fun <T : Any> invokeWithList(
        method: NotificationCompat.Builder.(T) -> NotificationCompat.Builder,
        provider: NotificationValueProvider<List<T>>,
        builder: NotificationCompat.Builder,
        pushMessage: PushMessage
    ) {
        provider.get(pushMessage)?.let { values ->
            values.forEach { method.invoke(builder, it) }
        }
    }
}
