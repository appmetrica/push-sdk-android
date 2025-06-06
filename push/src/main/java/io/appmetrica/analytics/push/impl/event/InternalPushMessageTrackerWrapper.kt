package io.appmetrica.analytics.push.impl.event

import io.appmetrica.analytics.push.event.AdditionalActionPushEvent
import io.appmetrica.analytics.push.event.DismissPushEvent
import io.appmetrica.analytics.push.event.ExpiredPushEvent
import io.appmetrica.analytics.push.event.IgnoredPushEvent
import io.appmetrica.analytics.push.event.InlineAdditionalActionPushEvent
import io.appmetrica.analytics.push.event.OpenPushEvent
import io.appmetrica.analytics.push.event.ProcessSilentPushEvent
import io.appmetrica.analytics.push.event.PushEvent
import io.appmetrica.analytics.push.event.ReceivePushEvent
import io.appmetrica.analytics.push.event.RemovedPushEvent
import io.appmetrica.analytics.push.event.ReplacePushEvent
import io.appmetrica.analytics.push.event.ShownPushEvent
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker

class InternalPushMessageTrackerWrapper(
    private val listener: InternalPushMessageTracker
) {

    fun reportPushEvent(pushEvent: PushEvent) {
        when (pushEvent) {
            is AdditionalActionPushEvent ->
                listener.onNotificationAdditionalAction(
                    pushEvent.pushId,
                    pushEvent.actionId,
                    pushEvent.payload,
                    pushEvent.transport,
                    pushEvent.targetActionUri,
                )
            is DismissPushEvent ->
                listener.onNotificationCleared(
                    pushEvent.pushId,
                    pushEvent.payload,
                    pushEvent.transport,
                )
            is ExpiredPushEvent ->
                listener.onNotificationExpired(
                    pushEvent.pushId,
                    pushEvent.category,
                    pushEvent.payload,
                    pushEvent.transport,
                )
            is IgnoredPushEvent ->
                listener.onNotificationIgnored(
                    pushEvent.pushId,
                    pushEvent.category,
                    pushEvent.details,
                    pushEvent.payload,
                    pushEvent.transport,
                )
            is InlineAdditionalActionPushEvent ->
                listener.onNotificationInlineAdditionalAction(
                    pushEvent.pushId,
                    pushEvent.actionId,
                    pushEvent.payload,
                    pushEvent.text,
                    pushEvent.transport,
                    pushEvent.targetActionUri,
                )
            is OpenPushEvent ->
                listener.onPushOpened(
                    pushEvent.pushId,
                    pushEvent.payload,
                    pushEvent.transport,
                    pushEvent.targetActionUri,
                )
            is RemovedPushEvent ->
                listener.onRemovingSilentPushProcessed(
                    pushEvent.pushId,
                    pushEvent.category,
                    pushEvent.details,
                    pushEvent.payload,
                    pushEvent.transport,
                )
            is ProcessSilentPushEvent ->
                listener.onSilentPushProcessed(
                    pushEvent.pushId,
                    pushEvent.payload,
                    pushEvent.transport,
                )
            is ReceivePushEvent ->
                listener.onMessageReceived(
                    pushEvent.pushId,
                    pushEvent.payload,
                    pushEvent.transport,
                )
            is ReplacePushEvent ->
                listener.onNotificationReplace(
                    pushEvent.pushId,
                    pushEvent.newPushId,
                    pushEvent.transport,
                )
            is ShownPushEvent ->
                listener.onNotificationShown(
                    pushEvent.pushId,
                    pushEvent.payload,
                    pushEvent.transport,
                )
        }
    }
}
