package io.appmetrica.analytics.push.impl.lazypush

import android.content.Context
import io.appmetrica.analytics.push.impl.lazypush.provider.CustomLazyPushTransformRuleProvider
import io.appmetrica.analytics.push.impl.lazypush.provider.DeviceIdTransformRuleProvider
import io.appmetrica.analytics.push.impl.lazypush.provider.LocationTransformRuleProvider
import io.appmetrica.analytics.push.impl.lazypush.provider.PushIdTransformRuleProvider
import io.appmetrica.analytics.push.impl.lazypush.provider.UuidTransformRuleProvider
import io.appmetrica.analytics.push.impl.utils.StringTransform
import io.appmetrica.analytics.push.model.PushMessage

internal class DefaultLazyPushTransformRuleProviders {

    fun getAsStringTransform(
        context: Context,
        pushMessage: PushMessage
    ): StringTransform {
        // location transform is the last since it is the most time expensive operation
        val defaultRuleProviders = listOf(
            DeviceIdTransformRuleProvider(context),
            PushIdTransformRuleProvider(),
            UuidTransformRuleProvider(context),
            LocationTransformRuleProvider(context),
            CustomLazyPushTransformRuleProvider()
        )
        val stringTransform = StringTransform("[{]", "[}]")
        defaultRuleProviders.mapNotNull { it.getRule(pushMessage) }.forEach {
            stringTransform.rule(it)
        }
        return stringTransform
    }
}
