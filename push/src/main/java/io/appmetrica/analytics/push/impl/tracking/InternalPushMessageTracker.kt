package io.appmetrica.analytics.push.impl.tracking

interface InternalPushMessageTracker {

    fun onPushTokenInited(
        value: String,
        transport: String
    )

    fun onPushTokenUpdated(
        value: String,
        transport: String
    )

    fun onMessageReceived(
        pushId: String,
        payload: String?,
        transport: String
    )

    fun onNotificationCleared(
        pushId: String,
        payload: String?,
        transport: String
    )

    fun onPushOpened(
        pushId: String,
        payload: String?,
        transport: String,
        uri: String?
    )

    fun onNotificationAdditionalAction(
        pushId: String,
        actionId: String?,
        payload: String?,
        transport: String,
        uri: String?
    )

    fun onSilentPushProcessed(
        pushId: String,
        payload: String?,
        transport: String
    )

    fun onNotificationInlineAdditionalAction(
        pushId: String,
        actionId: String?,
        payload: String?,
        text: String,
        transport: String,
        uri: String?
    )

    fun onNotificationShown(
        pushId: String,
        payload: String?,
        transport: String
    )

    fun onNotificationIgnored(
        pushId: String,
        category: String?,
        details: String?,
        payload: String?,
        transport: String
    )

    fun onNotificationExpired(
        pushId: String,
        category: String?,
        payload: String?,
        transport: String
    )

    fun onRemovingSilentPushProcessed(
        pushId: String,
        category: String?,
        details: String?,
        payload: String?,
        transport: String
    )

    fun onNotificationReplace(
        pushId: String,
        newPushId: String?,
        transport: String
    )
}
