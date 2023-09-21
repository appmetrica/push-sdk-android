package io.appmetrica.analytics.push.notification.providers

import android.content.Context
import android.content.pm.ApplicationInfo
import io.appmetrica.analytics.push.impl.Constants
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.model.PushNotification
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SmallIconProviderTest {

    private val applicationInfoIconResId = randomInt()
    private val context: Context = mock()
    private val applicationInfo: ApplicationInfo = mock {
        it.icon = applicationInfoIconResId
    }
    private val pushMessage: PushMessage = mock()
    private val notification: PushNotification = mock()

    @get:Rule
    val utils = MockedStaticRule(Utils::class.java)

    private val provider = SmallIconProvider(context)

    @Before
    fun setUp() {
        whenever(context.applicationInfo).thenReturn(applicationInfo)
    }

    @Test
    fun get() {
        val iconResId = randomInt()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.iconResId).thenReturn(iconResId)

        assertThat(provider.get(pushMessage)).isEqualTo(iconResId)
    }

    @Test
    fun getIfIconResIdIsNull() {
        val iconResId = randomInt()

        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.iconResId).thenReturn(null)
        whenever(Utils.getIntegerFromMetaData(context, Constants.DEFAULT_ICON_META_DATA_NAME)).thenReturn(iconResId)

        assertThat(provider.get(pushMessage)).isEqualTo(iconResId)
    }

    @Test
    fun getIfIntegerFromMetadataIsNull() {
        whenever(pushMessage.notification).thenReturn(notification)
        whenever(notification.iconResId).thenReturn(null)
        whenever(Utils.getIntegerFromMetaData(context, Constants.DEFAULT_ICON_META_DATA_NAME)).thenReturn(null)

        assertThat(provider.get(pushMessage)).isEqualTo(applicationInfoIconResId)
    }

    @Test
    fun getIfNotificationIsNull() {
        val iconResId = randomInt()

        whenever(pushMessage.notification).thenReturn(null)
        whenever(Utils.getIntegerFromMetaData(context, Constants.DEFAULT_ICON_META_DATA_NAME)).thenReturn(iconResId)

        assertThat(provider.get(pushMessage)).isEqualTo(iconResId)
    }
}
