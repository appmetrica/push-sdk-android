package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import org.junit.Test

class RemovedPushEventTest {

    private val transport = "test_transport"
    private val pushId = "test_push_id"
    private val payload = "test_payload"
    private val category = "test_category"
    private val details = "test_details"

    @Test
    fun `creates with all fields populated`() {
        val event = RemovedPushEvent(
            pushId,
        ).withTransport(transport)
            .withPayload(payload)
            .withCategory(category)
            .withDetails(details)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("payload", payload)
            .checkField("category", category)
            .checkField("details", details)
            .checkAll()
    }

    @Test
    fun `creates with all nullable fields null`() {
        val event = RemovedPushEvent(
            pushId,
        )

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
}
