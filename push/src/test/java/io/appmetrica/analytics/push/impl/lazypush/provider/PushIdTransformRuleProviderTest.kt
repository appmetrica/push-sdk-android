package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.PushIdTransformRule
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class PushIdTransformRuleProviderTest : CommonTest() {

    private val pushIdTransformRuleProvider = PushIdTransformRuleProvider()

    @Test
    fun getRule() {
        assertThat(pushIdTransformRuleProvider.getRule(mock())).isInstanceOf(PushIdTransformRule::class.java)
    }
}
