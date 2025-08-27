package io.appmetrica.analytics.push.impl.token.filter.provider

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.token.filter.AndCompositeTokenEventFilter
import io.appmetrica.analytics.push.impl.token.filter.IsForceTokenEventFilter
import io.appmetrica.analytics.push.impl.token.filter.MinIntervalTokenEventFilter
import io.appmetrica.analytics.push.impl.token.filter.OrCompositeTokenEventFilter
import io.appmetrica.analytics.push.impl.token.filter.PushServiceControllerTokenEventFilter
import io.appmetrica.analytics.push.impl.token.filter.TokenEventFilter

class DefaultTokenEventFilterProvider : TokenEventFilterProvider {

    override fun getTokenEventFilter(
        context: Context,
        bundle: Bundle
    ): TokenEventFilter {
        return OrCompositeTokenEventFilter(
            IsForceTokenEventFilter(),
            AndCompositeTokenEventFilter(
                MinIntervalTokenEventFilter(AppMetricaPushCore.getInstance(context).tokenManager),
                PushServiceControllerTokenEventFilter(AppMetricaPushCore.getInstance(context).pushServiceController)
            ),
        )
    }
}
