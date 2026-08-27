package io.appmetrica.analytics.push.event

import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.gradle.testutils.CommonTest
import io.appmetrica.gradle.testutils.assertions.Assertions.ObjectPropertyAssertions
import org.junit.Test

class ReplacePushEventTest : CommonTest() {

    private val transport = "test_transport"
    private val pushId = "test_push_id"
    private val newPushId = "test_newPushId"

    @Test
    fun `creates with all fields populated`() {
        val event = ReplacePushEvent(
            pushId,
        ).withTransport(transport)
            .withNewPushId(newPushId)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("newPushId", newPushId)
            .checkAll()
    }

    @Test
    fun `creates with all nullable fields null`() {
        val event = ReplacePushEvent(
            pushId,
        )

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", CoreConstants.Transport.UNKNOWN)
            .checkField("pushId", pushId)
            .checkFieldIsNull("newPushId")
            .checkAll()
    }
}
