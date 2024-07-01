package io.appmetrica.analytics.push.notification.providers

import android.app.PendingIntent
import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PushServiceProvider
import io.appmetrica.analytics.push.intent.NotificationActionInfo
import io.appmetrica.analytics.push.intent.NotificationActionType
import io.appmetrica.analytics.push.internal.IntentHelper
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DeleteIntentProviderTest {

    private val context: Context = mock()
    private val pushMessage: PushMessage = mock()

    private val autoTrackingConfiguration = AutoTrackingConfiguration.newBuilder().build()

    private val pushServiceProvider: PushServiceProvider = mock {
        on { autoTrackingConfiguration } doReturn autoTrackingConfiguration
    }

    @get:Rule
    val intentHelperRule = MockedStaticRule(IntentHelper::class.java)

    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { pushServiceProvider } doReturn pushServiceProvider
    }

    @get:Rule
    val appMetricaPushCoreMockedStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val provider = DeleteIntentProvider(context)

    @Test
    fun get() {
        val notificationActionInfo: NotificationActionInfo = mock()
        val pendingIntent: PendingIntent = mock()

        whenever(IntentHelper.createNotificationActionInfo(eq(NotificationActionType.CLEAR), eq(pushMessage), eq(null)))
            .thenReturn(notificationActionInfo)
        whenever(IntentHelper.createWrappedAction(eq(context), eq(notificationActionInfo), eq(true)))
            .thenReturn(pendingIntent)

        assertThat(provider.get(pushMessage)).isEqualTo(pendingIntent)
        intentHelperRule.staticMock.verify {
            IntentHelper.createNotificationActionInfo(eq(NotificationActionType.CLEAR), eq(pushMessage), eq(null))
            IntentHelper.createWrappedAction(eq(context), eq(notificationActionInfo), eq(true))
        }
    }
}
