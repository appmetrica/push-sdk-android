package io.appmetrica.analytics.push.impl.system.event

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.commands.SystemInfoCommandInfo
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.notification.NotificationStatus
import io.appmetrica.analytics.push.impl.notification.NotificationStatusProvider
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultSystemInfoEventProviderTest : CommonTest() {

    private val context: Context = mock()

    private val notificationStatusProvider: NotificationStatusProvider = mock()
    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { notificationStatusProvider } doReturn notificationStatusProvider
    }
    @get:Rule
    val appmetricaPushCoreRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val provider = DefaultSystemInfoEventProvider()

    @Test
    fun getSystemInfoEvent() {
        val notificationStatus: NotificationStatus = mock()
        whenever(notificationStatusProvider.notificationStatus).thenReturn(notificationStatus)

        val bundle = Bundle()
        val event = provider.getSystemInfoEvent(context, bundle)

        assertThat(event!!.notificationStatus).isSameAs(notificationStatus)
    }

    @Test
    fun getSystemInfoEventWithUpdateSystemInfo() {
        val notificationStatus: NotificationStatus = mock()
        whenever(notificationStatusProvider.notificationStatus).thenReturn(notificationStatus)

        val statusChangeTime = 234324L
        val systemInfoCommandInfo = SystemInfoCommandInfo.Builder()
            .withStatusChangeTime(statusChangeTime)
            .build()

        val bundle = Bundle().apply {
            putBundle(Commands.UpdateSystemInfo.EXTRA_INFO, systemInfoCommandInfo.toBundle())
        }
        val event = provider.getSystemInfoEvent(context, bundle)

        assertThat(event!!.notificationStatus).isSameAs(notificationStatus)
        verify(notificationStatus).setChangedTime(statusChangeTime)
    }
}
