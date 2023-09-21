package io.appmetrica.analytics.push.notification.providers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.impl.Constants
import io.appmetrica.analytics.push.impl.utils.PendingIntentFlagHelper
import io.appmetrica.analytics.push.impl.utils.RequestCodeUtils
import io.appmetrica.analytics.push.internal.receiver.TtlBroadcastReceiver
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.assertj.core.data.Offset
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class TimeoutProviderTest {

    private val context: Context = mock()
    private val alarmManager: AlarmManager = mock()
    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val requestCodeUtilsMock = MockedStaticRule(RequestCodeUtils::class.java)
    @get:Rule
    val pendingIntentFlagHelperMock = MockedStaticRule(PendingIntentFlagHelper::class.java)
    @get:Rule
    val pendingIntentMock = MockedStaticRule(PendingIntent::class.java)

    private val provider = TimeoutProvider(context)

    @Before
    fun setUp() {
        whenever(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager)
    }

    @Test
    fun getIfNotificationTtlIsLess() {
        val notificationTtl = 10000L
        val timeToHide = System.currentTimeMillis() + 20000L

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.notificationTtl).thenReturn(notificationTtl)
        whenever(notification.timeToHideMillis).thenReturn(timeToHide)

        assertThat(provider.get(pushMessage)).isEqualTo(10000L)
        verifyZeroInteractions(alarmManager)
    }

    @Test
    fun getIfTimeToHideIsLess() {
        val notificationTtl = 20000L
        val timeToHide = System.currentTimeMillis() + 10000L

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.notificationTtl).thenReturn(notificationTtl)
        whenever(notification.timeToHideMillis).thenReturn(timeToHide)

        assertThat(provider.get(pushMessage)).isCloseTo(10000, Offset.offset(1000))
        verifyZeroInteractions(alarmManager)
    }

    @Test
    fun getIfNotificationTtlIsNull() {
        val timeToHide = System.currentTimeMillis() + 10000L

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.notificationTtl).thenReturn(null)
        whenever(notification.timeToHideMillis).thenReturn(timeToHide)

        assertThat(provider.get(pushMessage)).isCloseTo(10000, Offset.offset(1000))
        verifyZeroInteractions(alarmManager)
    }

    @Test
    fun getIfTimeToHideIsNull() {
        val notificationTtl = 10000L

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.notificationTtl).thenReturn(notificationTtl)
        whenever(notification.timeToHideMillis).thenReturn(null)

        assertThat(provider.get(pushMessage)).isEqualTo(10000L)
        verifyZeroInteractions(alarmManager)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.N_MR1])
    fun getIfApiNotAchived() {
        val notificationTtl = 10000L
        val pushId = randomString()
        val notificationId = randomInt()
        val notificationTag = randomString()
        val payload = randomString()
        val transport = randomString()
        val requestCode = randomInt()
        val pendingIntentFlag = randomInt()

        val pendingIntent: PendingIntent = mock()
        whenever(PendingIntent.getBroadcast(
            eq(context),
            eq(requestCode),
            any(),
            eq(pendingIntentFlag)
        )).thenReturn(pendingIntent)

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.notificationTtl).thenReturn(notificationTtl)
        whenever(notification.timeToHideMillis).thenReturn(null)

        whenever(pushMessage.notificationId).thenReturn(pushId)
        whenever(notification.notificationId).thenReturn(notificationId)
        whenever(notification.notificationTag).thenReturn(notificationTag)
        whenever(pushMessage.payload).thenReturn(payload)
        whenever(pushMessage.transport).thenReturn(transport)
        whenever(RequestCodeUtils.incrementAndGet(context)).thenReturn(requestCode)
        whenever(PendingIntentFlagHelper.getPendingIntentFlag(PendingIntent.FLAG_CANCEL_CURRENT, false))
            .thenReturn(pendingIntentFlag)

        val triggerAtMillisCaptor = ArgumentCaptor.forClass(Long::class.java)
        val pendingIntentCaptor = ArgumentCaptor.forClass(PendingIntent::class.java)
        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)

        assertThat(provider.get(pushMessage)).isNull()
        verify(alarmManager).set(
            eq(AlarmManager.RTC),
            triggerAtMillisCaptor.capture(),
            pendingIntentCaptor.capture()
        )
        assertThat(triggerAtMillisCaptor.value)
            .isCloseTo(System.currentTimeMillis() + notificationTtl, Offset.offset(1000))
        assertThat(pendingIntentCaptor.value).isEqualTo(pendingIntent)
        pendingIntentMock.staticMock.verify {
            PendingIntent.getBroadcast(
                eq(context),
                eq(requestCode),
                intentCaptor.capture(),
                eq(pendingIntentFlag)
            )
        }

        val intent = intentCaptor.value
        SoftAssertions().apply {
            assertThat(intent.action).isEqualTo(TtlBroadcastReceiver.EXPIRED_BY_TTL_ACTION)
            assertThat(intent.extras).isNotNull
            assertThat(intent.extras!!.getString(Constants.PUSH_ID))
                .`as`("PUSH_ID")
                .isEqualTo(pushId)
            assertThat(intent.extras!!.getInt(Constants.NOTIFICATION_ID))
                .`as`("NOTIFICATION_ID")
                .isEqualTo(notificationId)
            assertThat(intent.extras!!.getString(Constants.NOTIFICATION_TAG))
                .`as`("NOTIFICATION_TAG")
                .isEqualTo(notificationTag)
            assertThat(intent.extras!!.getString(Constants.PAYLOAD))
                .`as`("PAYLOAD")
                .isEqualTo(payload)
            assertThat(intent.extras!!.getString(CoreConstants.EXTRA_TRANSPORT))
                .`as`("EXTRA_TRANSPORT")
                .isEqualTo(transport)
            assertAll()
        }
    }
}
