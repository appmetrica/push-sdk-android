package io.appmetrica.analytics.push.notification.providers

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class ChannelIdProviderTest {

    private val context: Context = mock()
    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val notificationChannelController: NotificationChannelController = mock()

    private val provider = ChannelIdProvider(context)

    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { notificationChannelController } doReturn notificationChannelController
    }

    @get:Rule
    val appMetricaPushCoreMockedStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    @Test
    fun get() {
        val channelId = Rand.randomString()
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.channelId).thenReturn(channelId)

        assertThat(provider.get(pushMessage)).isEqualTo(channelId)
        verifyNoInteractions(notificationChannelController)
    }

    @Test
    fun getIfEmpty() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.channelId).thenReturn("")

        assertThat(provider.get(pushMessage)).isEqualTo(NotificationChannelController.DEFAULT_CHANNEL_ID)
        verify(notificationChannelController).createDefaultChannel()
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isEqualTo(NotificationChannelController.DEFAULT_CHANNEL_ID)
        verify(notificationChannelController).createDefaultChannel()
    }
}
