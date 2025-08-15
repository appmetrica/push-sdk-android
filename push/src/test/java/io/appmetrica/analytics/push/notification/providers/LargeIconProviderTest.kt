package io.appmetrica.analytics.push.notification.providers

import android.graphics.Bitmap
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LargeIconProviderTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    private val provider = LargeIconProvider()

    @Test
    fun get() {
        val bitmap: Bitmap = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.largeIcon).thenReturn(bitmap)

        assertThat(provider.get(pushMessage)).isEqualTo(bitmap)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
