package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ColorProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = ColorProvider()

    @Test
    fun get() {
        val color = Rand.randomInt()
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.color).thenReturn(color)

        assertThat(provider.get(pushMessage)).isEqualTo(color)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
