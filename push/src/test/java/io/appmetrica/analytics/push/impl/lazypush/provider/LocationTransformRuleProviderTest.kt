package io.appmetrica.analytics.push.impl.lazypush.provider

import android.content.Context
import io.appmetrica.analytics.push.impl.lazypush.rule.LocationTransformRule
import io.appmetrica.analytics.push.model.LazyPushRequestInfo
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class LocationTransformRuleProviderTest : CommonTest() {

    private val context: Context = mock()
    private val lazyPushRequestInfo: LazyPushRequestInfo = mock()
    private val pushMessage: PushMessage = mock {
        whenever(it.lazyPushRequestInfo).thenReturn(lazyPushRequestInfo)
    }

    private val locationTransformRuleProvider = LocationTransformRuleProvider(context)

    @Test
    fun getRule() {
        assertThat(locationTransformRuleProvider.getRule(pushMessage)).isInstanceOf(LocationTransformRule::class.java)
    }

    @Test
    fun getRuleIfLazyPushRequestInfoIsNull() {
        whenever(pushMessage.lazyPushRequestInfo).thenReturn(null)

        assertThat(locationTransformRuleProvider.getRule(pushMessage)).isNull()
    }
}
