package io.appmetrica.analytics.push.impl.event

import io.appmetrica.analytics.push.event.PushEvent
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker
import io.appmetrica.analytics.push.testutils.CommonTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class InternalPushMessageTrackerWrapperTest : CommonTest() {

    private val actionId = "action_1"
    private val category = "category_1"
    private val details = "details_1"
    private val newPushId = "new_push_111"
    private val payload = "{\"key\":\"value\"}"
    private val pushId = "push_123"
    private val text = "test_text"
    private val transport = "test_transport"
    private val uri = "https://example.com"

    private val mockTracker: InternalPushMessageTracker = mock()
    private val wrapper = InternalPushMessageTrackerWrapper(mockTracker)

    @Test
    fun `report with AdditionalActionPushEvent delegates to onNotificationAdditionalAction`() {
        val event = PushEvent.additionalActionEvent(
            pushId,
            actionId
        ).withTransport(transport)
            .withTargetActionUri(uri)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationAdditionalAction(
            pushId = pushId,
            actionId = actionId,
            uri = uri,
            payload = payload,
            transport = transport
        )
    }

    @Test
    fun `report with DismissPushEvent delegates to onNotificationCleared`() {
        val event = PushEvent.dismissEvent(
            pushId
        ).withTransport(transport)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationCleared(
            pushId,
            payload,
            transport
        )
    }

    @Test
    fun `report with ExpiredPushEvent delegates to onNotificationExpired`() {
        val event = PushEvent.expiredEvent(
            pushId
        ).withTransport(transport)
            .withPayload(payload)
            .withCategory(category)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationExpired(
            pushId,
            category,
            payload,
            transport
        )
    }

    @Test
    fun `report with IgnoredPushEvent delegates to onNotificationIgnored`() {
        val event = PushEvent.ignoredEvent(
            pushId,
            category,
            details
        ).withTransport(transport)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationIgnored(
            pushId,
            category,
            details,
            payload,
            transport
        )
    }

    @Test
    fun `report with InlineAdditionalActionPushEvent does nothing`() {
        val event = PushEvent.inlineAdditionalActionEvent(
            pushId,
            actionId,
            text
        ).withTransport(transport)
            .withTargetActionUri(uri)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationInlineAdditionalAction(
            pushId,
            actionId,
            payload,
            text,
            transport,
            uri
        )
    }

    @Test
    fun `report with OpenPushEvent delegates to onPushOpened`() {
        val event = PushEvent.openEvent(
            pushId
        ).withTransport(transport)
            .withTargetActionUri(uri)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onPushOpened(
            pushId,
            payload,
            transport,
            uri
        )
    }

    @Test
    fun `report with RemovedPushEvent delegates to onRemovingSilentPushProcessed`() {
        val event = PushEvent.removedEvent(
            pushId
        ).withTransport(transport)
            .withPayload(payload)
            .withCategory(category)
            .withDetails(details)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onRemovingSilentPushProcessed(
            pushId,
            category,
            details,
            payload,
            transport
        )
    }

    @Test
    fun `report with ProcessSilentPushEvent delegates to onSilentPushProcessed`() {
        val event = PushEvent.processSilentEvent(
            pushId
        ).withTransport(transport)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onSilentPushProcessed(
            pushId,
            payload,
            transport
        )
    }

    @Test
    fun `report with ReceivePushEvent delegates to onMessageReceived`() {
        val event = PushEvent.receiveEvent(
            pushId
        ).withTransport(transport)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onMessageReceived(
            pushId,
            payload,
            transport
        )
    }

    @Test
    fun `report with ReplacePushEvent does nothing`() {
        val event = PushEvent.replaceEvent(
            pushId,
            newPushId
        ).withTransport(transport)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationReplace(
            pushId,
            newPushId,
            transport
        )
    }

    @Test
    fun `report with ShownPushEvent delegates to onNotificationShown`() {
        val event = PushEvent.shownEvent(
            pushId
        ).withTransport(transport)
            .withPayload(payload)

        wrapper.reportPushEvent(event)

        verify(mockTracker).onNotificationShown(
            pushId,
            payload,
            transport
        )
    }
}
