package io.appmetrica.analytics.push.impl.notification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.service.notification.StatusBarNotification
import android.util.Pair
import io.appmetrica.analytics.push.MockablePushServiceProvider
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PushMessageHistory.PushInfo
import io.appmetrica.analytics.push.impl.PushServiceProvider
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.Rand
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class PushIdFinderTests : CommonTest() {

    private val trackersHub: TrackersHub = mock()

    @get:Rule
    val trackersHubMockedStaticRule = staticRule<TrackersHub> {
        on { TrackersHub.getInstance() } doReturn trackersHub
    }

    private val notificationManager: NotificationManager = mock()

    private val context: Context = mock {
        on { getSystemService(Context.NOTIFICATION_SERVICE) } doReturn notificationManager
    }

    private val pushServiceProvider: PushServiceProvider = MockablePushServiceProvider()
    private val pushMessageHistory = pushServiceProvider.pushMessageHistory

    private var appMetricaPushCore: AppMetricaPushCore = mock {
        on { pushServiceProvider } doReturn pushServiceProvider
        on { pushMessageHistory } doReturn pushMessageHistory
    }

    @get:Rule
    val appMetricaPushCoreMockedStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val pushIdFinder by setUp { PushIdFinder(context) }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP_MR1])
    fun testCallGetPushInfoByNotificationTagAndNotificationId() {
        val tag = Rand.randomString()
        val id = Rand.randomInt()
        pushIdFinder.findActive(tag, id)
        verify(pushMessageHistory).getPushInfoByNotificationTagAndNotificationId(
            eq(tag),
            eq(id)
        )
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP_MR1])
    fun testNotFoundPushInfo() {
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(null)
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isNull()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP_MR1])
    fun testPushInfoNotContainsIsActive() {
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(Rand.randomString(), null)
        )
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isNull()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP_MR1])
    fun testPushInfoIsNotActive() {
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(Rand.randomString(), false)
        )
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isNull()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP_MR1])
    fun testPushInfoIsActive() {
        val pushId = Rand.randomString()
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(pushId, true)
        )
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isEqualTo(pushId)
    }

    @Test
    fun testPushInfoIsActiveAndNotificationInStatusBar() {
        val pushId = Rand.randomString()
        val tag = Rand.randomString()
        val id = Rand.randomInt()
        mockStatusBarNotification(tag, id)
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(pushId, true)
        )
        assertThat(pushIdFinder.findActive(tag, id)).isEqualTo(pushId)
        verify(trackersHub, never()).reportError(any(), any())
    }

    @Test
    fun testPushInfoIsNotActiveAndNotificationInStatusBar() {
        val pushId = Rand.randomString()
        val tag = Rand.randomString()
        val id = Rand.randomInt()
        mockStatusBarNotification(tag, id)
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(pushId, false)
        )
        assertThat(pushIdFinder.findActive(tag, id)).isEqualTo(pushId)
        verify(trackersHub).reportError(any(), anyOrNull())
    }

    @Test
    fun testPushInfoNotFoundAndNotificationInStatusBar() {
        val tag = Rand.randomString()
        val id = Rand.randomInt()
        mockStatusBarNotification(tag, id)
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(null)
        assertThat(pushIdFinder.findActive(tag, id)).isNull()
        verify(trackersHub).reportError(
            any(), anyOrNull()
        )
    }

    @Test
    fun testPushInfoIsActiveAndNotificationNotInStatusBar() {
        val pushId = Rand.randomString()
        mockStatusBarNotification(Rand.randomString(), Rand.randomInt())
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(pushId, true)
        )
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isNull()
        verify(trackersHub).reportError(
            any(), anyOrNull()
        )
    }

    @Test
    fun testPushInfoIsNotActiveAndNotificationNotInStatusBar() {
        val pushId = Rand.randomString()
        mockStatusBarNotification(Rand.randomString(), Rand.randomInt())
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(
            createPushInfo(pushId, false)
        )
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isNull()
        verify(trackersHub, never()).reportError(
            any(), any()
        )
    }

    @Test
    fun testPushInfoNotFoundAndNotificationNotInStatusBar() {
        mockStatusBarNotification(Rand.randomString(), Rand.randomInt())
        whenever(
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(
                any(),
                anyInt()
            )
        ).thenReturn(null)
        assertThat(pushIdFinder.findActive(Rand.randomString(), Rand.randomInt()))
            .isNull()
        verify(trackersHub, never()).reportError(
            any(), any()
        )
    }

    private fun createPushInfo(pushId: String, isActive: Boolean?): PushInfo {
        return PushInfo(pushId, Rand.randomInt(), Rand.randomString(), isActive)
    }

    private fun mockStatusBarNotification(tag: String, id: Int) {
        mockStatusBarNotification(arrayOf(Pair.create(tag, id)))
    }

    private fun mockStatusBarNotification(ids: Array<Pair<String, Int>>) {
        val notifications = arrayOfNulls<StatusBarNotification>(ids.size)
        for (i in ids.indices) {
            val notification = mock(
                StatusBarNotification::class.java
            )
            whenever(notification.tag).thenReturn(ids[i].first)
            whenever(notification.id).thenReturn(ids[i].second)
            notifications[i] = notification
        }
        whenever(notificationManager.activeNotifications).thenReturn(notifications)
    }
}
