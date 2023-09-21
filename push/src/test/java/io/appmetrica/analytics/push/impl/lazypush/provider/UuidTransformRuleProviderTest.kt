package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.UuidTransformRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class UuidTransformRuleProviderTest {

    private val uuidTransformRuleProvider = UuidTransformRuleProvider(mock())

    @Test
    fun getRule() {
        assertThat(uuidTransformRuleProvider.getRule(mock())).isInstanceOf(UuidTransformRule::class.java)
    }
}
