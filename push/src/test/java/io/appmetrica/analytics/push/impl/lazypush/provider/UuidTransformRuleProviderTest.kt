package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.UuidTransformRule
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class UuidTransformRuleProviderTest : CommonTest() {

    private val uuidTransformRuleProvider = UuidTransformRuleProvider(mock())

    @Test
    fun getRule() {
        assertThat(uuidTransformRuleProvider.getRule(mock())).isInstanceOf(UuidTransformRule::class.java)
    }
}
