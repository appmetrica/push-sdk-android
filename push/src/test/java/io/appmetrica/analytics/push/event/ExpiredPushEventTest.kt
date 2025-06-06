package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import org.junit.Test

class ExpiredPushEventTest {

    private val transport = "test_transport"
    private val pushId = "test_push_id"
    private val payload = "test_payload"
    private val category = "test_category"

    @Test
    fun `creates with all fields`() {
        val event = ExpiredPushEvent(
            pushId,
        ).withTransport(transport)
            .withPayload(payload)
            .withCategory(category)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("payload", payload)
            .checkField("category", category)
            .checkAll()
    }

    @Test
    fun `creates with all nullable fields null`() {
        val event = ExpiredPushEvent(
            pushId,
        )

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("payload")
            .checkFieldIsNull("category")
            .checkAll()
    }
}
