package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.PushIdTransformRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class PushIdTransformRuleProviderTest {

    private val pushIdTransformRuleProvider = PushIdTransformRuleProvider()

    @Test
    fun getRule() {
        assertThat(pushIdTransformRuleProvider.getRule(mock())).isInstanceOf(PushIdTransformRule::class.java)
    }
}
