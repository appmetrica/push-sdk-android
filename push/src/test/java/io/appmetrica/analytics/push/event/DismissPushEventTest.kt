package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import org.junit.Test

class DismissPushEventTest {

    private val transport = "test_transport"
    private val pushId = "test_push_id"
    private val payload = "test_payload"

    @Test
    fun `creates with all fields`() {
        val event = DismissPushEvent(
            pushId,
        ).withTransport(transport)
            .withPayload(payload)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("payload", payload)
            .checkAll()
    }

    @Test
    fun `creates with all nullable fields null`() {
        val event = DismissPushEvent(
            pushId,
        )

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkAll()
    }
}
