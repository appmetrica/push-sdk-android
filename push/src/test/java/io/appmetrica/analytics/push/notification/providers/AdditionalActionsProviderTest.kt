package io.appmetrica.analytics.push.notification.providers

import android.app.PendingIntent
import android.content.Context
import android.os.Build
import io.appmetrica.analytics.push.impl.notification.processing.InlineActionProcessingStrategy
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.internal.IntentHelper
import io.appmetrica.analytics.push.model.AdditionalAction
import io.appmetrica.analytics.push.model.AdditionalActionType
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class AdditionalActionsProviderTest {

    private val context: Context = mock()
    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val intentHelperRule = MockedStaticRule(IntentHelper::class.java)

    private val provider = AdditionalActionsProvider(context)

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfAllEmpty() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.additionalActions).thenReturn(emptyArray())

        assertThat(provider.get(pushMessage)).isEmpty()
    }

    @Test
    fun getIfNotInline() {
        val additionalAction: AdditionalAction = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.additionalActions).thenReturn(arrayOf(additionalAction))

        val actionInfo: NotificationActionInfo = mock()
        whenever(IntentHelper.createNotificationActionInfo(eq(pushMessage), eq(additionalAction)))
            .thenReturn(actionInfo)

        val actionIntent: PendingIntent = mock()
        whenever(IntentHelper.getPendingIntentForAdditionalAction(eq(context), eq(additionalAction), eq(actionInfo)))
            .thenReturn(actionIntent)

        val additionalActionTitle = randomString()
        val label = randomString()
        whenever(additionalAction.type).thenReturn(AdditionalActionType.OPEN_URI)
        whenever(additionalAction.title).thenReturn(additionalActionTitle)
        whenever(additionalAction.label).thenReturn(label)

        val action = provider.get(pushMessage)!!.first()
        SoftAssertions().apply {
            assertThat(action.actionIntent).isEqualTo(actionIntent)
            assertThat(action.icon).isEqualTo(0)
            assertThat(action.title).isEqualTo(additionalActionTitle)
            assertAll()
        }
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun getIfInlineAndApiIsNotAchived() {
        val additionalAction: AdditionalAction = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.additionalActions).thenReturn(arrayOf(additionalAction))

        val actionInfo: NotificationActionInfo = mock()
        whenever(IntentHelper.createNotificationActionInfo(eq(pushMessage), eq(additionalAction)))
            .thenReturn(actionInfo)

        val actionIntent: PendingIntent = mock()
        whenever(IntentHelper.getPendingIntentForAdditionalAction(eq(context), eq(additionalAction), eq(actionInfo)))
            .thenReturn(actionIntent)

        val additionalActionTitle = randomString()
        val label = randomString()
        whenever(additionalAction.type).thenReturn(AdditionalActionType.INLINE)
        whenever(additionalAction.title).thenReturn(additionalActionTitle)
        whenever(additionalAction.label).thenReturn(label)

        assertThat(provider.get(pushMessage)).isEmpty()
    }

    @Test
    fun getIfInlineAndLabelIsEmpty() {
        val additionalAction: AdditionalAction = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.additionalActions).thenReturn(arrayOf(additionalAction))

        val actionInfo: NotificationActionInfo = mock()
        whenever(IntentHelper.createNotificationActionInfo(eq(pushMessage), eq(additionalAction)))
            .thenReturn(actionInfo)

        val actionIntent: PendingIntent = mock()
        whenever(IntentHelper.getPendingIntentForAdditionalAction(eq(context), eq(additionalAction), eq(actionInfo)))
            .thenReturn(actionIntent)

        val additionalActionTitle = randomString()
        whenever(additionalAction.type).thenReturn(AdditionalActionType.INLINE)
        whenever(additionalAction.title).thenReturn(additionalActionTitle)
        whenever(additionalAction.label).thenReturn("")

        assertThat(provider.get(pushMessage)).isEmpty()
    }

    @Test
    fun getIfInline() {
        val additionalAction: AdditionalAction = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.additionalActions).thenReturn(arrayOf(additionalAction))

        val actionInfo: NotificationActionInfo = mock()
        whenever(IntentHelper.createNotificationActionInfo(eq(pushMessage), eq(additionalAction)))
            .thenReturn(actionInfo)

        val actionIntent: PendingIntent = mock()
        whenever(IntentHelper.getPendingIntentForAdditionalAction(eq(context), eq(additionalAction), eq(actionInfo)))
            .thenReturn(actionIntent)

        val additionalActionTitle = randomString()
        val label = randomString()
        whenever(additionalAction.type).thenReturn(AdditionalActionType.INLINE)
        whenever(additionalAction.title).thenReturn(additionalActionTitle)
        whenever(additionalAction.label).thenReturn(label)

        val action = provider.get(pushMessage)!!.first()
        SoftAssertions().apply {
            assertThat(action.actionIntent).isEqualTo(actionIntent)
            assertThat(action.icon).isEqualTo(0)
            assertThat(action.title).isEqualTo(additionalActionTitle)
            assertThat(action.remoteInputs).hasSize(1)
            assertThat(action.remoteInputs!!.first().resultKey).isEqualTo(InlineActionProcessingStrategy.KEY_TEXT_REPLY)
            assertThat(action.remoteInputs!!.first().label).isEqualTo(label)
            assertAll()
        }
    }

    @Test
    fun getManyActions() {
        val additionalAction: AdditionalAction = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.additionalActions).thenReturn(arrayOf(additionalAction, additionalAction))

        val actionInfo: NotificationActionInfo = mock()
        whenever(IntentHelper.createNotificationActionInfo(eq(pushMessage), eq(additionalAction)))
            .thenReturn(actionInfo)

        val actionIntent: PendingIntent = mock()
        whenever(IntentHelper.getPendingIntentForAdditionalAction(eq(context), eq(additionalAction), eq(actionInfo)))
            .thenReturn(actionIntent)

        val additionalActionTitle = randomString()
        val label = randomString()
        whenever(additionalAction.type).thenReturn(AdditionalActionType.OPEN_URI)
        whenever(additionalAction.title).thenReturn(additionalActionTitle)
        whenever(additionalAction.label).thenReturn(label)

        val actions = provider.get(pushMessage)!!
        assertThat(actions).hasSize(2)

        SoftAssertions().apply {
            actions.forEach { action ->
                assertThat(action.actionIntent).isEqualTo(actionIntent)
                assertThat(action.icon).isEqualTo(0)
                assertThat(action.title).isEqualTo(additionalActionTitle)
                assertAll()
            }
        }
    }
}
