package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.testutils.CommonTest
import org.junit.Test

class InlineAdditionalActionPushEventTest : CommonTest() {

    private val transport = "test_transport"
    private val pushId = "test_push_id"
    private val actionId = "test_action_id"
    private val targetActionUri = "test_uri"
    private val payload = "test_payload"
    private val text = "test_text"

    @Test
    fun `creates with all fields`() {
        val event = InlineAdditionalActionPushEvent(
            pushId,
            text,
        ).withTransport(transport)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("actionId", actionId)
            .checkField("targetActionUri", targetActionUri)
            .checkField("payload", payload)
            .checkField("text", text)
            .checkAll()
    }

    @Test
    fun `creates with all nullable fields null`() {
        val event = InlineAdditionalActionPushEvent(
            pushId,
            text,
        )

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("actionId")
            .checkFieldIsNull("targetActionUri")
            .checkFieldIsNull("payload")
            .checkField("text", text)
            .checkAll()
    }
}
