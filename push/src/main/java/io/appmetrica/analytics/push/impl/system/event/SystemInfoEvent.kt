package io.appmetrica.analytics.push.impl.system.event

import io.appmetrica.analytics.push.impl.notification.NotificationStatus
import org.json.JSONObject

internal class SystemInfoEvent private constructor(
    val notificationStatus: NotificationStatus? = null,
) {

    private val notificationStatusKey = "notifications_status"

    fun toJson() = JSONObject().apply {
        put(notificationStatusKey, notificationStatus?.toJson())
    }

    internal class Builder {

        private var notificationStatus: NotificationStatus? = null

        fun withNotificationStatus(notificationStatus: NotificationStatus): Builder {
            this.notificationStatus = notificationStatus
            return this
        }

        fun build(): SystemInfoEvent {
            return SystemInfoEvent(
                notificationStatus = notificationStatus,
            )
        }
    }
}
