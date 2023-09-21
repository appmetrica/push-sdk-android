package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand.randomLong
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.math.abs

class WhenProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = WhenProvider()

    @Test
    fun get() {
        val whenValue = randomLong()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.getWhen()).thenReturn(whenValue)

        assertThat(provider.get(pushMessage)).isEqualTo(whenValue)
    }

    @Test
    fun getIfNull() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.getWhen()).thenReturn(null)

        val currentTimeMillis = System.currentTimeMillis()
        val providerValue = provider.get(pushMessage)
        assertThat(providerValue).isNotNull
        assertThat(abs(providerValue!! - currentTimeMillis)).isLessThan(1000)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isCloseTo(System.currentTimeMillis(), Offset.offset(100))
    }
}
