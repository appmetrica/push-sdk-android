package io.appmetrica.analytics.push.notification.providers

import android.media.RingtoneManager
import android.net.Uri
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SoundProviderTest {

    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val ringtoneManager = MockedStaticRule(RingtoneManager::class.java)

    private val provider = SoundProvider()

    @Test
    fun get() {
        val soundUri: Uri = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.isSoundEnabled).thenReturn(true)
        whenever(notification.soundUri).thenReturn(soundUri)

        assertThat(provider.get(pushMessage)).isEqualTo(soundUri)
    }

    @Test
    fun getIfSoundUriIsNull() {
        val soundUri: Uri = mock()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.isSoundEnabled).thenReturn(true)
        whenever(notification.soundUri).thenReturn(null)
        whenever(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).thenReturn(soundUri)

        assertThat(provider.get(pushMessage)).isEqualTo(soundUri)
    }

    @Test
    fun getIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(provider.get(pushMessage)).isNull()
    }

    @Test
    fun getIfSoundEnabledIsFalse() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.isSoundEnabled).thenReturn(false)

        assertThat(provider.get(pushMessage)).isNull()
    }
}
