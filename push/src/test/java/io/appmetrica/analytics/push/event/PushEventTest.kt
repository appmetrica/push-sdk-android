package io.appmetrica.analytics.push.event

import android.content.Intent
import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.impl.event.IntentToPushEventConverter
import io.appmetrica.analytics.push.testutils.constructionRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify

class PushEventTest {

    private val pushId = "test_push_id"
    private val actionId = "action_id"
    private val category = "test_category"
    private val text = "test_text"
    private val details = "test_details"
    private val newPushId = "new_push_id"

    @get:Rule
    val intentToPushEventConverterRule = constructionRule<IntentToPushEventConverter>()

    @Test
    fun `creates AdditionalActionPushEvent with all fields`() {
        val event = PushEvent.additionalActionEvent(pushId, actionId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkField("actionId", actionId)
            .checkFieldIsNull("targetActionUri")
            .checkFieldIsNull("payload")
            .checkAll()
    }

    @Test
    fun `creates DismissPushEvent with all fields`() {
        val event = PushEvent.dismissEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkAll()
    }

    @Test
    fun `creates ExpiredPushEvent with all fields`() {
        val event = PushEvent.expiredEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkFieldIsNull("category")
            .checkAll()
    }

    @Test
    fun `creates IgnoredPushEvent with all fields`() {
        val event = PushEvent.ignoredEvent(pushId, category, details)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkField("category", category)
            .checkField("details", details)
            .checkAll()
    }

    @Test
    fun `creates InlineAdditionalActionPushEvent with all fields`() {
        val event = PushEvent.inlineAdditionalActionEvent(pushId, actionId, text)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkField("actionId", actionId)
            .checkFieldIsNull("targetActionUri")
            .checkFieldIsNull("payload")
            .checkField("text", text)
            .checkAll()
    }

    @Test
    fun `creates OpenPushEvent with target action uri and payload`() {
        val event = PushEvent.openEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("targetActionUri")
            .checkFieldIsNull("payload")
            .checkAll()
    }

    @Test
    fun `creates RemovedPushEvent with all fields`() {
        val event = PushEvent.removedEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkFieldIsNull("category")
            .checkFieldIsNull("details")
            .checkAll()
    }

    @Test
    fun `creates ProcessSilentPushEvent with all fields`() {
        val event = PushEvent.processSilentEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkAll()
    }

    @Test
    fun `creates ReceivePushEvent with all fields`() {
        val event = PushEvent.receiveEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkAll()
    }

    @Test
    fun `creates ReplacePushEvent with all fields`() {
        val event = PushEvent.replaceEvent(pushId, newPushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkField("newPushId", newPushId)
            .checkAll()
    }

    @Test
    fun `creates ShownPushEvent with all fields`() {
        val event = PushEvent.shownEvent(pushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkAll()
    }

    @Test
    fun `creates PushEvent from Intent`() {
        val intent = Intent()
        PushEvent.fromIntent(intent)

        val converter = intentToPushEventConverterRule.constructionMock.constructed().firstOrNull()

        assertThat(converter).isNotNull
        verify(converter)!!.convert(intent)
    }
}
