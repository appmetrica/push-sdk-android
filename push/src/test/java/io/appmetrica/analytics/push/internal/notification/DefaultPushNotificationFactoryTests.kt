package io.appmetrica.analytics.push.internal.notification

import android.content.Context
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.nullable
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class DefaultPushNotificationFactoryTests {

    @get:Rule
    val pushMessageTrackerHubRule = MockedStaticRule(PushMessageTrackerHub::class.java)

    private lateinit var context: Context
    private val pushMessage: PushMessage = mock()
    private val pushNotification: PushNotification = mock()
    private val pushMessageTracker: PushMessageTrackerHub = mock()

    private var factory = DefaultPushNotificationFactory()

    @Before
    fun setUp() {
        context = Mockito.spy(RuntimeEnvironment.application.applicationContext)
        whenever(pushMessage.notificationId).thenReturn(randomString())
        whenever(PushMessageTrackerHub.getInstance()).thenReturn(pushMessageTracker)
        factory = DefaultPushNotificationFactory()
    }

    @Test
    fun buildNotification() {
        whenever(pushMessage.notification).thenReturn(pushNotification)
        whenever(pushNotification.contentTitle).thenReturn(randomString())
        whenever(pushNotification.contentText).thenReturn(randomString())

        assertThat(factory.buildNotification(context, pushMessage)).isNotNull
    }

    @Test
    fun buildNotificationIfEmptyTitle() {
        whenever(pushMessage.notification).thenReturn(pushNotification)
        whenever(pushNotification.contentTitle).thenReturn("")
        whenever(pushNotification.contentText).thenReturn(randomString())

        assertThat(factory.buildNotification(context, pushMessage)).isNotNull
    }

    @Test
    fun buildNotificationIfEmptyText() {
        whenever(pushMessage.notification).thenReturn(pushNotification)
        whenever(pushNotification.contentTitle).thenReturn(randomString())
        whenever(pushNotification.contentText).thenReturn("")

        assertThat(factory.buildNotification(context, pushMessage)).isNotNull
    }

    @Test
    fun buildNotificationReturnsNullIfEmptyTitleAndText() {
        whenever(pushMessage.notification).thenReturn(pushNotification)
        whenever(pushNotification.contentTitle).thenReturn("")
        whenever(pushNotification.contentText).thenReturn("")

        assertThat(factory.buildNotification(context, pushMessage)).isNull()
        verify(pushMessageTracker).onNotificationIgnored(
            anyString(),
            anyString(),
            anyString(),
            nullable(String::class.java),
            nullable(String::class.java)
        )
    }

    @Test
    fun buildNotificationReturnsNullIfNotificationIsNull() {
        whenever(pushMessage.notification).thenReturn(null)

        assertThat(factory.buildNotification(context, pushMessage)).isNull()
    }
}
