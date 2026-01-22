package io.appmetrica.analytics.push.impl.event

import io.appmetrica.analytics.push.event.AdditionalActionPushEvent
import io.appmetrica.analytics.push.event.DismissPushEvent
import io.appmetrica.analytics.push.event.ExpiredPushEvent
import io.appmetrica.analytics.push.event.IgnoredPushEvent
import io.appmetrica.analytics.push.event.InlineAdditionalActionPushEvent
import io.appmetrica.analytics.push.event.OpenPushEvent
import io.appmetrica.analytics.push.event.ProcessSilentPushEvent
import io.appmetrica.analytics.push.event.PushEvent
import io.appmetrica.analytics.push.event.PushEventListener
import io.appmetrica.analytics.push.event.ReceivePushEvent
import io.appmetrica.analytics.push.event.RemovedPushEvent
import io.appmetrica.analytics.push.event.ReplacePushEvent
import io.appmetrica.analytics.push.event.ShownPushEvent
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class PushEventListenerWrapperTest : CommonTest() {

    private val actionId = "action_1"
    private val category = "category_1"
    private val details = "details_1"
    private val newPushId = "new_push_111"
    private val payload = "{\"key\":\"value\"}"
    private val pushId = "push_123"
    private val text = "test_text"
    private val transport = "test_transport"
    private val uri = "https://example.com"
    private val value = "test_token"

    private val mockListener: PushEventListener = mock()
    private val wrapper = PushEventListenerWrapper(mockListener)

    @Test
    fun `onMessageReceived should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<ReceivePushEvent>()

        wrapper.onMessageReceived(pushId, payload, transport)

        verify(mockListener).onPushReceived(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.receiveEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    @Test
    fun `onNotificationCleared should delegate to listener with dismiss event`() {
        val pushEventCaptor = argumentCaptor<DismissPushEvent>()

        wrapper.onNotificationCleared(pushId, payload, transport)

        verify(mockListener).onNotificationDismissed(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.dismissEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    @Test
    fun `onPushOpened should delegate to listener with open event`() {
        val pushEventCaptor = argumentCaptor<OpenPushEvent>()

        wrapper.onPushOpened(pushId, payload, transport, uri)

        verify(mockListener).onNotificationOpened(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.openEvent(pushId)
                .withTransport(transport)
                .withTargetActionUri(uri)
                .withPayload(payload)
        )
    }

    @Test
    fun `onNotificationAdditionalAction should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<AdditionalActionPushEvent>()

        wrapper.onNotificationAdditionalAction(pushId, actionId, payload, transport, uri)

        verify(mockListener).onNotificationAdditionalAction(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.additionalActionEvent(pushId, actionId)
                .withTransport(transport)
                .withTargetActionUri(uri)
                .withPayload(payload)
        )
    }

    @Test
    fun `onSilentPushProcessed should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<ProcessSilentPushEvent>()

        wrapper.onSilentPushProcessed(pushId, payload, transport)

        verify(mockListener).onSilentPushProcessed(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.processSilentEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    @Test
    fun `onNotificationInlineAdditionalAction should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<InlineAdditionalActionPushEvent>()

        wrapper.onNotificationInlineAdditionalAction(pushId, actionId, payload, text, transport, uri)

        verify(mockListener).onNotificationInlineAdditionalAction(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.inlineAdditionalActionEvent(pushId, actionId, text)
                .withTransport(transport)
                .withTargetActionUri(uri)
                .withPayload(payload)
        )
    }

    @Test
    fun `onNotificationShown should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<ShownPushEvent>()

        wrapper.onNotificationShown(pushId, payload, transport)

        verify(mockListener).onNotificationShown(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.shownEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    @Test
    fun `onNotificationIgnored should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<IgnoredPushEvent>()

        wrapper.onNotificationIgnored(pushId, category, details, payload, transport)

        verify(mockListener).onNotificationIgnored(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.ignoredEvent(pushId, category, details)
                .withTransport(transport)
                .withPayload(payload)
        )
    }

    @Test
    fun `onNotificationExpired should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<ExpiredPushEvent>()

        wrapper.onNotificationExpired(pushId, category, payload, transport)

        verify(mockListener).onNotificationExpired(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.expiredEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
                .withCategory(category)
        )
    }

    @Test
    fun `onRemovingSilentPushProcessed should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<RemovedPushEvent>()

        wrapper.onRemovingSilentPushProcessed(pushId, category, details, payload, transport)

        verify(mockListener).onNotificationRemoved(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.removedEvent(pushId)
                .withTransport(transport)
                .withPayload(payload)
                .withCategory(category)
                .withDetails(details)
        )
    }

    @Test
    fun `onNotificationReplace should delegate to listener with correct event`() {
        val pushEventCaptor = argumentCaptor<ReplacePushEvent>()

        wrapper.onNotificationReplace(pushId, newPushId, transport)

        verify(mockListener).onNotificationReplace(pushEventCaptor.capture())

        assertThat(pushEventCaptor.firstValue).usingRecursiveComparison().isEqualTo(
            PushEvent.replaceEvent(pushId, newPushId)
                .withTransport(transport)
        )
    }
}
