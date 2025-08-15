package io.appmetrica.analytics.push.impl.tracking

import io.appmetrica.analytics.push.settings.PushMessageTracker
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class PushMessageTrackerWrapperTest : CommonTest() {

    private val value: String = randomString()
    private val transport: String = randomString()
    private val pushId: String = randomString()
    private val payload: String = randomString()
    private val uri: String = randomString()
    private val actionId: String = randomString()
    private val text: String = randomString()
    private val details: String = randomString()
    private val category: String = randomString()

    private val tracker: PushMessageTracker = mock()

    private val wrapper: InternalPushMessageTracker by lazy {
        PushMessageTrackerWrapper(tracker)
    }

    @Test
    fun onPushTokenInited() {
        wrapper.onPushTokenInited(value, transport)
        verify(tracker).onPushTokenInited(value, transport)
    }

    @Test
    fun onPushTokenUpdated() {
        wrapper.onPushTokenUpdated(value, transport)
        verify(tracker).onPushTokenUpdated(value, transport)
    }

    @Test
    fun onMessageReceived() {
        wrapper.onMessageReceived(pushId, payload, transport)
        verify(tracker).onMessageReceived(pushId, payload, transport)
    }

    @Test
    fun onNotificationCleared() {
        wrapper.onNotificationCleared(pushId, payload, transport)
        verify(tracker).onNotificationCleared(pushId, payload, transport)
    }

    @Test
    fun onPushOpened() {
        wrapper.onPushOpened(pushId, payload, transport, uri)
        verify(tracker).onPushOpened(pushId, payload, transport)
    }

    @Test
    fun onNotificationAdditionalAction() {
        wrapper.onNotificationAdditionalAction(pushId, actionId, payload, transport, uri)
        verify(tracker).onNotificationAdditionalAction(pushId, actionId, payload, transport)
    }

    @Test
    fun onSilentPushProcessed() {
        wrapper.onSilentPushProcessed(pushId, payload, transport)
        verify(tracker).onSilentPushProcessed(pushId, payload, transport)
    }

    @Test
    fun onNotificationInlineAdditionalAction() {
        wrapper.onNotificationInlineAdditionalAction(pushId, actionId, payload, text, transport, uri)
        verify(tracker).onNotificationInlineAdditionalAction(pushId, actionId, payload, text, transport)
    }

    @Test
    fun onNotificationShown() {
        wrapper.onNotificationShown(pushId, payload, transport)
        verify(tracker).onNotificationShown(pushId, payload, transport)
    }

    @Test
    fun onNotificationIgnored() {
        wrapper.onNotificationIgnored(pushId, category, details, payload, transport)
        verify(tracker).onNotificationIgnored(pushId, category, details, payload, transport)
    }

    @Test
    fun onNotificationExpired() {
        wrapper.onNotificationExpired(pushId, category, payload, transport)
        verify(tracker).onNotificationExpired(pushId, category, payload, transport)
    }

    @Test
    fun onRemovingSilentPushProcessed() {
        wrapper.onRemovingSilentPushProcessed(pushId, category, details, payload, transport)
        verify(tracker).onRemovingSilentPushProcessed(pushId, category, details, payload, transport)
    }

    @Test
    fun onNotificationReplace() {
        wrapper.onNotificationReplace(pushId, payload, transport)
        verify(tracker).onNotificationReplace(pushId, payload, transport)
    }
}
