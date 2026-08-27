package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.gradle.testutils.CommonTest
import io.appmetrica.gradle.testutils.assertions.Assertions.ObjectPropertyAssertions
import org.junit.Test

class DismissPushEventTest : CommonTest() {

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
