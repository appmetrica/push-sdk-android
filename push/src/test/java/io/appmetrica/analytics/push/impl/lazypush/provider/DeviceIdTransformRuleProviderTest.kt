package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.DeviceIdTransformRule
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

internal class DeviceIdTransformRuleProviderTest : CommonTest() {

    private val deviceIdTransformRuleProvider = DeviceIdTransformRuleProvider(mock())

    @Test
    fun getRule() {
        assertThat(deviceIdTransformRuleProvider.getRule(mock())).isInstanceOf(DeviceIdTransformRule::class.java)
    }
}
