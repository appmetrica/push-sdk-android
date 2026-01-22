package io.appmetrica.analytics.push.impl.lazypush.provider

import android.content.Context
import io.appmetrica.analytics.push.impl.lazypush.rule.DeviceIdTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider
import io.appmetrica.analytics.push.model.PushMessage

internal class DeviceIdTransformRuleProvider(
    private val context: Context
) : LazyPushTransformRuleProvider {

    override fun getRule(pushMessage: PushMessage): LazyPushTransformRule {
        return DeviceIdTransformRule(context)
    }
}
