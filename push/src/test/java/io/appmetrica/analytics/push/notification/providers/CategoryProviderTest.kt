package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CategoryProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = CategoryProvider()

    @Test
    fun get() {
        val category = randomString()
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.category).thenReturn(category)

        assertThat(provider.get(pushMessage)).isEqualTo(category)
    }

    @Test
    fun getIfEmpty() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.category).thenReturn("")

        assertThat(provider.get(pushMessage)).isEqualTo("")
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
