package io.appmetrica.analytics.push.impl.processing

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.Constants
import io.appmetrica.analytics.push.impl.PushMessageHistory
import io.appmetrica.analytics.push.impl.PushServiceProvider
import io.appmetrica.analytics.push.impl.notification.PushIdFinder
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.impl.utils.ChannelHelper
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.settings.PushMessageTracker
import io.appmetrica.analytics.push.settings.PushNotificationFactory
import io.appmetrica.analytics.push.testutils.MockedConstructionRule
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.nullable
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class NotificationPublisherTest {

    @get:Rule
    val pushMessageTrackerHubRule = MockedStaticRule(PushMessageTrackerHub::class.java)
    @get:Rule
    val channelHelperRule = MockedStaticRule(ChannelHelper::class.java)
    @get:Rule
    val appMetricaPushCoreRule = MockedStaticRule(AppMetricaPushCore::class.java)

    @get:Rule
    val pushIdFinderConstructionRule = MockedConstructionRule(PushIdFinder::class.java) { mock, _ ->
        whenever(mock.findActive(
            pushNotification.notificationTag,
            notificationId
        )).thenReturn(oldPushId)
    }

    private val context: Context = mock()
    private val notificationManager: NotificationManager = mock()

    private val notificationFactory: PushNotificationFactory = mock()
    private val notification: Notification = mock()
    private val pushMessageTrackerHub: PushMessageTrackerHub = mock()
    private val pushMessageTracker: PushMessageTracker = mock()
    private val pushServiceProvider: PushServiceProvider = mock { mock ->
        whenever(mock.pushMessageTracker).thenReturn(pushMessageTracker)
    }
    private val pushMessageHistory: PushMessageHistory = mock()
    private val appMetricaPushCore: AppMetricaPushCore = mock { mock ->
        whenever(mock.pushServiceProvider).thenReturn(pushServiceProvider)
        whenever(mock.pushMessageHistory).thenReturn(pushMessageHistory)
    }

    private val oldPushId = randomString()
    private val notificationId = randomInt()
    private val pushNotification: PushNotification = mock { mock ->
        whenever(mock.notificationId).thenReturn(notificationId)
        whenever(mock.notificationTag).thenReturn(randomString())
    }
    private val pushMessage: PushMessage = mock { mock ->
        whenever(mock.notificationId).thenReturn(randomString())
        whenever(mock.transport).thenReturn(randomString())
        whenever(mock.notification).thenReturn(pushNotification)
        whenever(mock.payload).thenReturn(randomString())
    }

    private val publisher = NotificationPublisher()

    @Before
    fun setUp() {
        whenever(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager)
        whenever(notificationFactory.buildNotification(context, pushMessage)).thenReturn(notification)
        whenever(PushMessageTrackerHub.getInstance()).thenReturn(pushMessageTrackerHub)
        whenever(ChannelHelper.doesChannelExistForNotification(context, notification)).thenReturn(true)
        whenever(AppMetricaPushCore.getInstance(context)).thenReturn(appMetricaPushCore)
    }

    @Test
    fun publishNotification() {
        publisher.publishNotification(context, notificationFactory, pushMessage)

        verify(pushMessageTracker).onNotificationReplace(
            oldPushId,
            pushMessage.notificationId,
            pushMessage.transport
        )
        verify(pushMessageHistory).setPushActive(
            oldPushId,
            false
        )
        verify(notificationManager).notify(
            pushNotification.notificationTag,
            pushNotification.notificationId!!,
            notification
        )
        verify(pushMessageTrackerHub).onNotificationShown(
            pushMessage.notificationId!!,
            pushMessage.payload,
            pushMessage.transport
        )
        verify(pushMessageHistory).savePushInfo(
            pushMessage.notificationId!!,
            pushNotification.notificationId!!,
            pushNotification.notificationTag,
            true
        )
        verify(pushMessageHistory).savePushTimestamp(pushMessage)
    }

    @Test
    fun publishNotificationDoesNothingIfNotificationIsNull() {
        whenever(notificationFactory.buildNotification(context, pushMessage)).thenReturn(null)
        publisher.publishNotification(context, notificationFactory, pushMessage)

        verify(pushMessageTrackerHub).onNotificationIgnored(
            pushMessage.notificationId!!,
            Constants.IGNORED_CATEGORY_NOTIFICATION_IS_NULL,
            "",
            pushMessage.payload,
            pushMessage.transport
        )
    }

    @Test
    fun publishNotificationDoesNotReportReplaceIfNotNecessary() {
        whenever(pushNotification.notificationId).thenReturn(randomInt())
        publisher.publishNotification(context, notificationFactory, pushMessage)

        verify(pushMessageTracker, never()).onNotificationReplace(
            anyString(),
            anyString(),
            anyString()
        )
        verify(pushMessageHistory, never()).setPushActive(
            anyString(),
            anyBoolean()
        )
        verify(notificationManager).notify(
            pushNotification.notificationTag,
            pushNotification.notificationId!!,
            notification
        )
        verify(pushMessageTrackerHub).onNotificationShown(
            pushMessage.notificationId!!,
            pushMessage.payload,
            pushMessage.transport
        )
        verify(pushMessageHistory).savePushInfo(
            pushMessage.notificationId!!,
            pushNotification.notificationId!!,
            pushNotification.notificationTag,
            true
        )
        verify(pushMessageHistory).savePushTimestamp(pushMessage)
    }

    @Test
    fun publishNotificationReportsIgnoredIfChannelIsAbsent() {
        whenever(ChannelHelper.doesChannelExistForNotification(context, notification)).thenReturn(false)
        publisher.publishNotification(context, notificationFactory, pushMessage)

        verify(pushMessageTracker).onNotificationReplace(
            oldPushId,
            pushMessage.notificationId,
            pushMessage.transport
        )
        verify(pushMessageHistory).setPushActive(
            oldPushId,
            false
        )
        verifyZeroInteractions(
            notificationManager,
            pushMessageTrackerHub
        )
        verify(pushMessageHistory, never()).savePushInfo(
            anyString(),
            anyInt(),
            nullable(String::class.java),
            anyBoolean()
        )
        verify(pushMessageHistory, never()).savePushTimestamp(any())
        channelHelperRule.staticMock.verify {
            ChannelHelper.reportIgnoredSinceChannelIsAbsent(pushMessage, notification)
        }
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.N_MR1])
    fun publishNotificationDoesNotReportsIgnoredIfChannelIsAbsentAndOldAndroidVersion() {
        whenever(ChannelHelper.doesChannelExistForNotification(context, notification)).thenReturn(false)
        publisher.publishNotification(context, notificationFactory, pushMessage)

        verify(pushMessageTracker).onNotificationReplace(
            oldPushId,
            pushMessage.notificationId,
            pushMessage.transport
        )
        verify(pushMessageHistory).setPushActive(
            oldPushId,
            false
        )
        verify(notificationManager).notify(
            pushNotification.notificationTag,
            pushNotification.notificationId!!,
            notification
        )
        verify(pushMessageTrackerHub).onNotificationShown(
            pushMessage.notificationId!!,
            pushMessage.payload,
            pushMessage.transport
        )
        verify(pushMessageHistory).savePushInfo(
            pushMessage.notificationId!!,
            pushNotification.notificationId!!,
            pushNotification.notificationTag,
            true
        )
        verify(pushMessageHistory).savePushTimestamp(pushMessage)
        channelHelperRule.staticMock.verify({
            ChannelHelper.reportIgnoredSinceChannelIsAbsent(pushMessage, notification)
        }, never())
    }
}
