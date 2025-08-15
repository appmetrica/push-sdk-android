package io.appmetrica.analytics.push.notification.providers

import android.app.PendingIntent
import android.content.Context
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.intent.NotificationActionType
import io.appmetrica.analytics.push.internal.IntentHelper
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ContentIntentProviderTest : CommonTest() {

    private val context: Context = mock()
    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val intentHelperRule = MockedStaticRule(IntentHelper::class.java)

    private val provider = ContentIntentProvider(context)

    @Test
    fun get() {
        val action = randomString()
        val notificationActionInfo: NotificationActionInfo = mock()
        val pendingIntent: PendingIntent = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.openActionUrl).thenReturn(action)
        whenever(
            IntentHelper.createNotificationActionInfo(
                eq(NotificationActionType.CLICK),
                eq(pushMessage),
                eq(action)
            )
        ).thenReturn(notificationActionInfo)
        whenever(IntentHelper.getPendingIntentForOpenAction(eq(context), eq(notification), eq(notificationActionInfo)))
            .thenReturn(pendingIntent)

        assertThat(provider.get(pushMessage)).isEqualTo(pendingIntent)
        intentHelperRule.staticMock.verify {
            IntentHelper.createNotificationActionInfo(eq(NotificationActionType.CLICK), eq(pushMessage), eq(action))
            IntentHelper.getPendingIntentForOpenAction(eq(context), eq(notification), eq(notificationActionInfo))
        }
    }

    @Test
    fun getIfNotificationIsNull() {
        val notificationActionInfo: NotificationActionInfo = mock()
        val pendingIntent: PendingIntent = mock()

        whenever(pushMessage.notification).thenReturn(null)
        whenever(IntentHelper.createNotificationActionInfo(eq(NotificationActionType.CLICK), eq(pushMessage), eq(null)))
            .thenReturn(notificationActionInfo)
        whenever(IntentHelper.getPendingIntentForOpenAction(eq(context), eq(null), eq(notificationActionInfo)))
            .thenReturn(pendingIntent)

        assertThat(provider.get(pushMessage)).isEqualTo(pendingIntent)
        intentHelperRule.staticMock.verify {
            IntentHelper.createNotificationActionInfo(eq(NotificationActionType.CLICK), eq(pushMessage), eq(null))
            IntentHelper.getPendingIntentForOpenAction(eq(context), eq(null), eq(notificationActionInfo))
        }
    }
}
