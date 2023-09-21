package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.LazyPushTransformRuleProviderHolder
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CustomLazyPushTransformRuleProviderTest {

    private val pushMessage: PushMessage = mock()
    private val lazyPushTransformRule: LazyPushTransformRule = mock()
    private val lazyPushTransformRuleProvider: LazyPushTransformRuleProvider = mock {
        whenever(it.getRule(pushMessage)).thenReturn(lazyPushTransformRule)
    }

    private val customLazyPushTransformRuleProvider = CustomLazyPushTransformRuleProvider()

    @get:Rule
    val lazyPushTransformRuleProviderHolderRule = MockedStaticRule(LazyPushTransformRuleProviderHolder::class.java)

    @Test
    fun getRule() {
        whenever(LazyPushTransformRuleProviderHolder.provider).thenReturn(lazyPushTransformRuleProvider)

        assertThat(customLazyPushTransformRuleProvider.getRule(pushMessage)).isSameAs(lazyPushTransformRule)
    }

    @Test
    fun getRuleIfProviderIsNull() {
        whenever(LazyPushTransformRuleProviderHolder.provider).thenReturn(null)

        assertThat(customLazyPushTransformRuleProvider.getRule(pushMessage)).isNull()
    }
}
