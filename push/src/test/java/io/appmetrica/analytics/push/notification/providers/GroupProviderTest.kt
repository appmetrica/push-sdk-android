package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GroupProviderTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = GroupProvider()

    @Test
    fun get() {
        val group = randomString()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.group).thenReturn(group)

        assertThat(provider.get(pushMessage)).isEqualTo(group)
    }

    @Test
    fun getIfEmpty() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.group).thenReturn("")

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
