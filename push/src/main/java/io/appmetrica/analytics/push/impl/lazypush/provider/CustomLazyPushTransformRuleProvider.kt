package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.LazyPushTransformRuleProviderHolder
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider
import io.appmetrica.analytics.push.model.PushMessage

internal class CustomLazyPushTransformRuleProvider : LazyPushTransformRuleProvider {

    override fun getRule(pushMessage: PushMessage): LazyPushTransformRule? {
        return LazyPushTransformRuleProviderHolder.provider?.getRule(pushMessage)
    }
}
