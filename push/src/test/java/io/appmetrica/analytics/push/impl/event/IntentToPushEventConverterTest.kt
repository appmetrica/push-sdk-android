package io.appmetrica.analytics.push.impl.event

import android.content.Intent
import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.AppMetricaPush
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.intent.NotificationActionType
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IntentToPushEventConverterTest : CommonTest() {

    private val transport = "transport"
    private val pushId = "push id"
    private val actionId = "action id"
    private val targetActionUri = "target action uri"
    private val payload = "payload"

    private val converter by setUp { IntentToPushEventConverter() }

    @Test
    fun `convert clear action`() {
        val mockInfo = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.CLEAR)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val mockIntent = mock<Intent> {
            on {
                getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)
            } doReturn mockInfo
        }

        val event = converter.convert(mockIntent)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("payload", payload)
            .checkAll()
    }

    @Test
    fun `convert click action`() {
        val mockInfo = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.CLICK)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val mockIntent = mock<Intent> {
            on {
                getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)
            } doReturn mockInfo
        }

        val event = converter.convert(mockIntent)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("targetActionUri", targetActionUri)
            .checkField("payload", payload)
            .checkAll()
    }

    @Test
    fun `convert additional action`() {
        val mockInfo = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.ADDITIONAL_ACTION)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val mockIntent = mock<Intent> {
            on {
                getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)
            } doReturn mockInfo
        }

        val event = converter.convert(mockIntent)

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
    fun `convert inline action`() {
        val mockInfo = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.INLINE_ACTION)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val mockIntent = mock<Intent> {
            on {
                getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)
            } doReturn mockInfo
        }

        val event = converter.convert(mockIntent)

        ObjectPropertyAssertions(event)
            .withPrivateFields(true)
            .withFinalFieldOnly(false)
            .checkField("transport", transport)
            .checkField("pushId", pushId)
            .checkField("actionId", actionId)
            .checkField("targetActionUri", targetActionUri)
            .checkField("payload", payload)
            .checkField("text", "")
            .checkAll()
    }

    @Test
    fun `convert if notification action info is null`() {
        val mockIntent = mock<Intent> {
            on {
                getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)
            } doReturn null
        }

        val event = converter.convert(mockIntent)

        assertThat(event).isNull()
    }

    @Test
    fun `convert is push id is null`() {
        val mockInfo = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.INLINE_ACTION)
            .withPushId(null)
            .build()

        val mockIntent = mock<Intent> {
            on {
                getParcelableExtra<NotificationActionInfo?>(AppMetricaPush.EXTRA_ACTION_INFO)
            } doReturn mockInfo
        }

        val event = converter.convert(mockIntent)

        assertThat(event).isNull()
    }
}
