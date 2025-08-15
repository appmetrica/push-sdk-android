package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GroupSummaryProviderTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = GroupSummaryProvider()

    @Test
    fun getIfTrue() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.groupSummary).thenReturn(true)

        assertThat(provider.get(pushMessage)).isTrue
    }

    @Test
    fun getIfFalse() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.groupSummary).thenReturn(false)

        assertThat(provider.get(pushMessage)).isFalse
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
