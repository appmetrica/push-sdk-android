package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class OngoingProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = OngoingProvider()

    @Test
    fun getIfTrue() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.ongoing).thenReturn(true)

        assertThat(provider.get(pushMessage)).isTrue
    }

    @Test
    fun getIfFalse() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.ongoing).thenReturn(false)

        assertThat(provider.get(pushMessage)).isFalse
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
