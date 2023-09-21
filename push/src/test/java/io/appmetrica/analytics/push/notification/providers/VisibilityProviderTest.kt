package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class VisibilityProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = VisibilityProvider()

    @Test
    fun get() {
        val visibility = randomInt()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.visibility).thenReturn(visibility)

        assertThat(provider.get(pushMessage)).isEqualTo(visibility)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
