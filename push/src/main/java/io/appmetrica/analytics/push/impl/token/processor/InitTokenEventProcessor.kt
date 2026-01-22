package io.appmetrica.analytics.push.impl.token.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.logger.internal.DebugLogger

internal class InitTokenEventProcessor : TokenEventProcessor {

    private val tag = "[InitTokenEventProcessor]"

    override fun process(
        context: Context,
        tokenEvent: TokenEvent
    ) {
        AppMetricaPushCore.getInstance(context).tokenManager
            ?.saveToken(tokenEvent.token, tokenEvent.provider, System.currentTimeMillis())
            ?: run {
                DebugLogger.warning(tag, "TokenManager is null")
            }

        PushMessageTrackerHub.getInstance().onPushTokenInited(
            tokenEvent.toJson().toString(),
            tokenEvent.provider
        )
    }
}
