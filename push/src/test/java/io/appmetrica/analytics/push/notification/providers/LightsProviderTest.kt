package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.LedLights
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LightsProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = LightsProvider()

    @Test
    fun get() {
        val ledLights: LedLights = mock()
        val color = randomInt()
        val onMs = randomInt()
        val offMs = randomInt()
        whenever(ledLights.color).thenReturn(color)
        whenever(ledLights.onMs).thenReturn(onMs)
        whenever(ledLights.offMs).thenReturn(offMs)
        whenever(ledLights.isValid).thenReturn(true)

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.ledLights).thenReturn(ledLights)

        assertThat(provider.get(pushMessage)).isEqualTo(listOf(color, onMs, offMs))
    }

    @Test
    fun getIfLedLightsIsNull() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.ledLights).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfNotValid() {
        val ledLights: LedLights = mock()
        whenever(ledLights.isValid).thenReturn(false)

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.ledLights).thenReturn(ledLights)

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
