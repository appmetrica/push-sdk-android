package io.appmetrica.analytics.push.impl.event

import android.content.Intent
import androidx.core.content.IntentCompat
import io.appmetrica.analytics.push.AppMetricaPush
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.intent.NotificationActionType
import io.appmetrica.gradle.testutils.CommonTest
import io.appmetrica.gradle.testutils.assertions.Assertions.ObjectPropertyAssertions
import io.appmetrica.gradle.testutils.rules.MockedStaticRule.Companion.on
import io.appmetrica.gradle.testutils.rules.MockedStaticRule.Companion.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class IntentToPushEventConverterTest : CommonTest() {

    private val transport = "transport"
    private val pushId = "push id"
    private val actionId = "action id"
    private val targetActionUri = "target action uri"
    private val payload = "payload"

    @get:Rule
    val intentCompatRule = staticRule<IntentCompat>()

    private val converter by setUp { IntentToPushEventConverter() }

    @Test
    fun `convert clear action`() {
        val info = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.CLEAR)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val event = converter.convert(wrapToIntent(info))

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
        val info = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.CLICK)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val event = converter.convert(wrapToIntent(info))

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
        val info = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.ADDITIONAL_ACTION)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val event = converter.convert(wrapToIntent(info))

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
        val info = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.INLINE_ACTION)
            .withPushId(pushId)
            .withActionId(actionId)
            .withTargetActionUri(targetActionUri)
            .withPayload(payload)
            .build()

        val event = converter.convert(wrapToIntent(info))

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
        val event = converter.convert(mock())

        assertThat(event).isNull()
    }

    @Test
    fun `convert is push id is null`() {
        val info = NotificationActionInfo.newBuilder(transport)
            .withActionType(NotificationActionType.INLINE_ACTION)
            .withPushId(null)
            .build()

        val event = converter.convert(wrapToIntent(info))

        assertThat(event).isNull()
    }

    private fun wrapToIntent(info: NotificationActionInfo): Intent {
        val intent = mock<Intent>()
        intentCompatRule.staticMock.on {
            IntentCompat.getParcelableExtra(
                intent, AppMetricaPush.EXTRA_ACTION_INFO, NotificationActionInfo::class.java
            )
        } doReturn info
        return intent
    }
}
