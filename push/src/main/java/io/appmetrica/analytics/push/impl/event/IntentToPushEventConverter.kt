package io.appmetrica.analytics.push.impl.event

import android.content.Intent
import io.appmetrica.analytics.push.AppMetricaPush
import io.appmetrica.analytics.push.event.PushEvent
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.intent.NotificationActionType

class IntentToPushEventConverter {

    fun convert(intent: Intent): PushEvent? {
        val info = intent.getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)

        if (info?.pushId == null) {
            return null
        }

        return when (info.actionType) {
            NotificationActionType.CLEAR -> PushEvent.dismissEvent(info.pushId)
                .withTransport(info.transport)
                .withPayload(info.payload)

            NotificationActionType.CLICK -> PushEvent.openEvent(info.pushId)
                .withTransport(info.transport)
                .withTargetActionUri(info.targetActionUri)
                .withPayload(info.payload)

            NotificationActionType.ADDITIONAL_ACTION -> PushEvent.additionalActionEvent(info.pushId, info.actionId)
                .withTransport(info.transport)
                .withTargetActionUri(info.targetActionUri)
                .withPayload(info.payload)

            NotificationActionType.INLINE_ACTION ->
                PushEvent.inlineAdditionalActionEvent(info.pushId, info.actionId, "")
                    .withTransport(info.transport)
                    .withTargetActionUri(info.targetActionUri)
                    .withPayload(info.payload)

            else -> null
        }
    }
}
