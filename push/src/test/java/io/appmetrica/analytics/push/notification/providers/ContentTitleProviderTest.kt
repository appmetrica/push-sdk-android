package io.appmetrica.analytics.push.notification.providers

import android.text.Spanned
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ContentTitleProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val utils = MockedStaticRule(Utils::class.java)

    private val provider = ContentTitleProvider()

    @Test
    fun get() {
        val contentTitle = randomString()
        val wrappedContentTitle: Spanned = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.contentTitle).thenReturn(contentTitle)
        whenever(Utils.wrapHtml(contentTitle)).thenReturn(wrappedContentTitle)

        assertThat(provider.get(pushMessage)).isEqualTo(wrappedContentTitle)
    }

    @Test
    fun getIfEmpty() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.contentTitle).thenReturn("")

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
