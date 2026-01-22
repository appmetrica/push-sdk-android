package io.appmetrica.analytics.push.impl.lazypush.provider

import io.appmetrica.analytics.push.impl.lazypush.rule.PushIdTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider
import io.appmetrica.analytics.push.model.PushMessage

internal class PushIdTransformRuleProvider : LazyPushTransformRuleProvider {

    override fun getRule(pushMessage: PushMessage): LazyPushTransformRule {
        return PushIdTransformRule(pushMessage.notificationId)
    }
}
