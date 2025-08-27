package io.appmetrica.analytics.push.impl.event

import io.appmetrica.analytics.push.event.PushEvent
import io.appmetrica.analytics.push.event.PushEventListener
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker

class PushEventListenerWrapper(
    private val listener: PushEventListener
) : InternalPushMessageTracker {

    override fun onPushTokenInited(value: String, transport: String) {
        // do nothing
    }

    override fun onPushTokenUpdated(value: String, transport: String) {
        // do nothing
    }

    override fun onSystemInfoUpdated(value: String) {
        // do nothing
    }

    override fun onMessageReceived(pushId: String, payload: String?, transport: String) {
        listener.onPushReceived(
            PushEvent.receiveEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    override fun onNotificationCleared(pushId: String, payload: String?, transport: String) {
        listener.onNotificationDismissed(
            PushEvent.dismissEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    override fun onPushOpened(pushId: String, payload: String?, transport: String, uri: String?) {
        listener.onNotificationOpened(
            PushEvent.openEvent(pushId)
                .withTransport(transport)
                .withTargetActionUri(uri)
                .withPayload(payload)
        )
    }

    override fun onNotificationAdditionalAction(
        pushId: String,
        actionId: String?,
        payload: String?,
        transport: String,
        uri: String?
    ) {
        listener.onNotificationAdditionalAction(
            PushEvent.additionalActionEvent(pushId, actionId)
                .withTransport(transport)
                .withTargetActionUri(uri)
                .withPayload(payload)
        )
    }

    override fun onSilentPushProcessed(pushId: String, payload: String?, transport: String) {
        listener.onSilentPushProcessed(
            PushEvent.processSilentEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    override fun onNotificationInlineAdditionalAction(
        pushId: String,
        actionId: String?,
        payload: String?,
        text: String,
        transport: String,
        uri: String?
    ) {
        listener.onNotificationInlineAdditionalAction(
            PushEvent.inlineAdditionalActionEvent(pushId, actionId, text)
                .withTransport(transport)
                .withTargetActionUri(uri)
                .withPayload(payload)
        )
    }

    override fun onNotificationShown(pushId: String, payload: String?, transport: String) {
        listener.onNotificationShown(
            PushEvent.shownEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    override fun onNotificationIgnored(
        pushId: String,
        category: String?,
        details: String?,
        payload: String?,
        transport: String
    ) {
        listener.onNotificationIgnored(
            PushEvent.ignoredEvent(pushId, "", "")
                .withTransport(transport)
                .withPayload(payload)
                .withCategory(category)
                .withDetails(details)
        )
    }

    override fun onNotificationExpired(pushId: String, category: String?, payload: String?, transport: String) {
        listener.onNotificationExpired(
            PushEvent.expiredEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
                .withCategory(category)
        )
    }

    override fun onRemovingSilentPushProcessed(
        pushId: String,
        category: String?,
        details: String?,
        payload: String?,
        transport: String
    ) {
        listener.onNotificationRemoved(
            PushEvent.removedEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
                .withCategory(category)
                .withDetails(details)
        )
    }

    override fun onNotificationReplace(pushId: String, newPushId: String?, transport: String) {
        listener.onNotificationReplace(
            PushEvent.replaceEvent(pushId, newPushId)
                .withTransport(transport)
        )
    }
}
