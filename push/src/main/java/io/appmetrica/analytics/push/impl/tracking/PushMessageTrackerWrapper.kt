package io.appmetrica.analytics.push.impl.tracking

import io.appmetrica.analytics.push.settings.PushMessageTracker

internal class PushMessageTrackerWrapper(
    private val tracker: PushMessageTracker
) : InternalPushMessageTracker {

    override fun onPushTokenInited(value: String, transport: String) =
        tracker.onPushTokenInited(value, transport)

    override fun onPushTokenUpdated(value: String, transport: String) =
        tracker.onPushTokenUpdated(value, transport)

    override fun onSystemInfoUpdated(value: String) {
        // do nothing
    }

    override fun onMessageReceived(pushId: String, payload: String?, transport: String) =
        tracker.onMessageReceived(pushId, payload, transport)

    override fun onNotificationCleared(pushId: String, payload: String?, transport: String) =
        tracker.onNotificationCleared(pushId, payload, transport)

    override fun onPushOpened(pushId: String, payload: String?, transport: String, uri: String?) =
        tracker.onPushOpened(pushId, payload, transport)

    override fun onNotificationAdditionalAction(
        pushId: String,
        actionId: String?,
        payload: String?,
        transport: String,
        uri: String?
    ) = tracker.onNotificationAdditionalAction(pushId, actionId, payload, transport)

    override fun onSilentPushProcessed(pushId: String, payload: String?, transport: String) =
        tracker.onSilentPushProcessed(pushId, payload, transport)

    override fun onNotificationInlineAdditionalAction(
        pushId: String,
        actionId: String?,
        payload: String?,
        text: String,
        transport: String,
        uri: String?
    ) = tracker.onNotificationInlineAdditionalAction(pushId, actionId, payload, text, transport)

    override fun onNotificationShown(pushId: String, payload: String?, transport: String) =
        tracker.onNotificationShown(pushId, payload, transport)

    override fun onNotificationIgnored(
        pushId: String,
        category: String?,
        details: String?,
        payload: String?,
        transport: String
    ) = tracker.onNotificationIgnored(pushId, category, details, payload, transport)

    override fun onNotificationExpired(pushId: String, category: String?, payload: String?, transport: String) =
        tracker.onNotificationExpired(pushId, category, payload, transport)

    override fun onRemovingSilentPushProcessed(
        pushId: String,
        category: String?,
        details: String?,
        payload: String?,
        transport: String
    ) = tracker.onRemovingSilentPushProcessed(pushId, category, details, payload, transport)

    override fun onNotificationReplace(pushId: String, newPushId: String?, transport: String) =
        tracker.onNotificationReplace(pushId, newPushId, transport)
}
