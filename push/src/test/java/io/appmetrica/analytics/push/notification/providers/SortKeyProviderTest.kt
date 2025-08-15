package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SortKeyProviderTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = SortKeyProvider()

    @Test
    fun get() {
        val sortKey = randomString()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.sortKey).thenReturn(sortKey)

        assertThat(provider.get(pushMessage)).isEqualTo(sortKey)
    }

    @Test
    fun getIfEmpty() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.sortKey).thenReturn("")

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
