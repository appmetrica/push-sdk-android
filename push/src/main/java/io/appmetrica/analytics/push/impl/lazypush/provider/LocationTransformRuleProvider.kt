package io.appmetrica.analytics.push.impl.lazypush.provider

import android.content.Context
import io.appmetrica.analytics.push.impl.lazypush.rule.LocationTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider
import io.appmetrica.analytics.push.model.PushMessage

class LocationTransformRuleProvider(
    private val context: Context
) : LazyPushTransformRuleProvider {

    override fun getRule(pushMessage: PushMessage): LazyPushTransformRule? {
        val lazyPushRequestInfo = pushMessage.lazyPushRequestInfo
        return lazyPushRequestInfo?.let {
            LocationTransformRule(context, it.locationRequestInfo)
        }
    }
}
