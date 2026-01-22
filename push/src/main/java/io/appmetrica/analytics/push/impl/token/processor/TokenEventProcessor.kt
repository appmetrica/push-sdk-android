package io.appmetrica.analytics.push.impl.token.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.token.event.TokenEvent

internal interface TokenEventProcessor {

    fun process(
        context: Context,
        tokenEvent: TokenEvent
    )
}
