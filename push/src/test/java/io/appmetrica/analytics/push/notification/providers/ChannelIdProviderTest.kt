package io.appmetrica.analytics.push.notification.providers

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever

class ChannelIdProviderTest {

    private val context: Context = mock()
    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val pushImpl = AppMetricaPushCore.getInstance(context)
    private val notificationChannelController: NotificationChannelController = mock()

    private val provider = ChannelIdProvider(context)

    @Before
    fun setUp() {
        pushImpl.notificationChannelController = notificationChannelController
    }

    @Test
    fun get() {
        val channelId = Rand.randomString()
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.channelId).thenReturn(channelId)

        assertThat(provider.get(pushMessage)).isEqualTo(channelId)
        verifyZeroInteractions(notificationChannelController)
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
