package io.appmetrica.analytics.push.impl.lazypush.rule

import android.content.Context
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.gradle.testutils.CommonTest
import io.appmetrica.gradle.testutils.data.Rand.randomString
import io.appmetrica.gradle.testutils.rules.MockedStaticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class DeviceIdTransformRuleTest : CommonTest() {

    private val context: Context = mock()

    private val deviceIdTransformRule = DeviceIdTransformRule(context)

    @get:Rule
    val appMetricaRule = MockedStaticRule(AppMetrica::class.java)

    @Test
    fun getNewValue() {
        val deviceId = randomString()
        whenever(AppMetrica.getDeviceId(context)).thenReturn(deviceId)

        assertThat(deviceIdTransformRule.getNewValue("deviceId")).isEqualTo(deviceId)
    }

    @Test
    fun getNewValueIfAppMetricaReturnNull() {
        whenever(AppMetrica.getDeviceId(context)).thenReturn(null)

        assertThat(deviceIdTransformRule.getNewValue("deviceId")).isEqualTo("")
    }

    @Test
    fun getNewValueIfAppMetricaThrowsException() {
        whenever(AppMetrica.getDeviceId(context)).thenThrow(IllegalAccessError())

        assertThat(deviceIdTransformRule.getNewValue("deviceId")).isEqualTo("")
    }
}
