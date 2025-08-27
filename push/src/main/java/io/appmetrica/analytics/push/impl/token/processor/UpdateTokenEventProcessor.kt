package io.appmetrica.analytics.push.impl.token.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub

class UpdateTokenEventProcessor : TokenEventProcessor {

    override fun process(
        context: Context,
        tokenEvent: TokenEvent
    ) {
        AppMetricaPushCore.getInstance(context).tokenManager
            ?.saveToken(tokenEvent.token, tokenEvent.provider, System.currentTimeMillis())

        PushMessageTrackerHub.getInstance().onPushTokenUpdated(
            tokenEvent.toJson().toString(),
            tokenEvent.provider
        )
    }
}
