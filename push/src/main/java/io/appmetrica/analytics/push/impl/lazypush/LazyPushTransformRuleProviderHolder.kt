package io.appmetrica.analytics.push.impl.lazypush

import io.appmetrica.analytics.push.lazypush.LazyPushTransformRuleProvider

internal object LazyPushTransformRuleProviderHolder {

    @JvmStatic
    var provider: LazyPushTransformRuleProvider? = null
}
