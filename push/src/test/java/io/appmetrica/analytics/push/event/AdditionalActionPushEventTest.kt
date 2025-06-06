package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import org.junit.Test

class AdditionalActionPushEventTest {

    private val transport = "test_transport"
    private val pushId = "test_push_id"
    private val actionId = "test_action_id"
    private val targetActionUri = "test_uri"
    private val payload = "test_payload"

    @Test
    fun `creates with all fields`() {
        val event = AdditionalActionPushEvent(
            pushId,
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
            .checkAll()
    }

    @Test
    fun `creates with all nullable fields null`() {
        val event = AdditionalActionPushEvent(
            pushId
        )

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("actionId")
            .checkFieldIsNull("targetActionUri")
            .checkFieldIsNull("payload")
            .checkAll()
    }
}
