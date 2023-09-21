package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.DeviceIdTransformRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class DeviceIdTransformRuleProviderTest {

    private val deviceIdTransformRuleProvider = DeviceIdTransformRuleProvider(mock())

    @Test
    fun getRule() {
        assertThat(deviceIdTransformRuleProvider.getRule(mock())).isInstanceOf(DeviceIdTransformRule::class.java)
    }
}
