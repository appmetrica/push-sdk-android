package io.appmetrica.analytics.push.notification.providers

import android.graphics.Bitmap
import android.media.RingtoneManager
import android.text.Spanned
import androidx.core.app.NotificationCompat
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class StyleProviderTest : CommonTest() {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val ringtoneManager = MockedStaticRule(RingtoneManager::class.java)
    @get:Rule
    val utils = MockedStaticRule(Utils::class.java)

    private val provider = StyleProvider()

    @Test
    fun get() {
        val bitmap: Bitmap = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.largeBitmap).thenReturn(bitmap)

        assertThat(provider.get(pushMessage))
            .usingRecursiveComparison()
            .isEqualTo(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
    }

    @Test
    fun getIfLargeBitmapIsNull() {
        val contentText = randomString()
        val wrappedText: Spanned = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.largeBitmap).thenReturn(null)
        whenever(notification.contentText).thenReturn(contentText)
        whenever(Utils.wrapHtml(contentText)).thenReturn(wrappedText)

        assertThat(provider.get(pushMessage))
            .isEqualToComparingFieldByField(NotificationCompat.BigTextStyle().bigText(wrappedText))
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
