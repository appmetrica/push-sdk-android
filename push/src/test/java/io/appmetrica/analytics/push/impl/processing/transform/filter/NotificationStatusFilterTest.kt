package io.appmetrica.analytics.push.impl.processing.transform.filter

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.Rand
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class NotificationStatusFilterTest : PushFilterTest() {
    private val notificationManager: NotificationManager = mock()
    private val notificationManagerCompat: NotificationManagerCompat = mock()

    @Before
    fun setUp() {
        super.setUp(NotificationStatusFilter(notificationManager, notificationManagerCompat))
        whenever(notificationManagerCompat.areNotificationsEnabled()).thenReturn(true)
    }

    @Test
    fun disabledAllNotification() {
        whenever(notificationManagerCompat.areNotificationsEnabled()).thenReturn(false)
        assertSilence(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(maxSdk = Build.VERSION_CODES.N_MR1)
    fun enabledNotificationOnApiLessOrEquals25() {
        assertShow(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    fun disabledNotificationChannel() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_NONE
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        assertSilence(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1])
    fun enabledNotificationOnApiEquals26Or27() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_DEFAULT
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        assertShow(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun enabledNotificationIfChannelNotExists() {
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(null)
        assertShow(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    fun disabledNotificationChannelGroup() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_DEFAULT
            on { group } doReturn Rand.randomString()
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        val group: NotificationChannelGroup = mock {
            on { isBlocked } doReturn true
        }
        whenever(notificationManager.getNotificationChannelGroup(ArgumentMatchers.anyString())).thenReturn(group)
        assertSilence(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    fun enabledNotificationOnApiMoreOrEquals28() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_DEFAULT
            on { group } doReturn Rand.randomString()
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        val group: NotificationChannelGroup = mock {
            on { isBlocked } doReturn false
        }
        whenever(notificationManager.getNotificationChannelGroup(ArgumentMatchers.anyString())).thenReturn(group)
        assertShow(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun enabledNotificationIfChannelGroupNotExists() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_DEFAULT
            on { group } doReturn Rand.randomString()
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        whenever(notificationManager.getNotificationChannelGroup(ArgumentMatchers.anyString())).thenReturn(null)
        assertShow(createPushMessage(Rand.randomString()))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun useDefaultChannelIdIfDefinedNull() {
        pushFilter.filter(createPushMessage(null))
        verify(notificationManager, Mockito.times(1))
            .getNotificationChannel(ArgumentMatchers.eq(NotificationChannelController.DEFAULT_CHANNEL_ID))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun useDefaultChannelIdIfDefinedEmpty() {
        pushFilter.filter(createPushMessage(""))
        verify(notificationManager, Mockito.times(1))
            .getNotificationChannel(ArgumentMatchers.eq(NotificationChannelController.DEFAULT_CHANNEL_ID))
    }

    @Test
    fun silentPushIfBlockedForAll() {
        val pushMessage = createPushMessage("Some")
        whenever(pushMessage.isSilent).thenReturn(true)
        assertShow(pushMessage)
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    fun silentPushIfBlockedNotificationChannelGroup() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_DEFAULT
            on { group } doReturn Rand.randomString()
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        val group: NotificationChannelGroup = mock {
            on { isBlocked } doReturn true
        }
        whenever(notificationManager.getNotificationChannelGroup(ArgumentMatchers.anyString())).thenReturn(group)
        val pushMessage = createPushMessage("Some")
        whenever(pushMessage.isSilent).thenReturn(true)

        assertShow(pushMessage)
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    fun silentPushIfDisabledNotificationChannel() {
        val channel: NotificationChannel = mock {
            on { importance } doReturn NotificationManager.IMPORTANCE_NONE
        }
        whenever(notificationManager.getNotificationChannel(ArgumentMatchers.anyString())).thenReturn(channel)
        val pushMessage = createPushMessage(Rand.randomString())
        whenever(pushMessage.isSilent).thenReturn(true)

        assertShow(pushMessage)
    }

    private fun createPushMessage(channelId: String?): PushMessage {
        val pushNotification: PushNotification = mock {
            on { getChannelId() } doReturn channelId
        }
        val pushMessage: PushMessage = mock {
            on { notification } doReturn pushNotification
        }
        return pushMessage
    }
}
