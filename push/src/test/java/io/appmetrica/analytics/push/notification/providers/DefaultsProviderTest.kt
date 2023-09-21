package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DefaultsProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = DefaultsProvider()

    @Test
    fun get() {
        val defaults = randomInt()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.defaults).thenReturn(defaults)

        assertThat(provider.get(pushMessage)).isEqualTo(defaults)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
