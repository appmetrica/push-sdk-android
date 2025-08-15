package io.appmetrica.analytics.push.notification.providers

import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class VibrateProviderTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = VibrateProvider()

    @Test
    fun get() {
        val vibrate = arrayOf(1L, 2L, 3L).toLongArray()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.vibrate).thenReturn(vibrate)

        assertThat(provider.get(pushMessage)).isEqualTo(vibrate)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
