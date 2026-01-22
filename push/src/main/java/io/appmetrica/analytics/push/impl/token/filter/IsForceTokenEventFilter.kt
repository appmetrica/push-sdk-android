package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.event.TokenEvent

internal class IsForceTokenEventFilter : TokenEventFilter {

    override val id = "isForce"

    override fun shouldSend(tokenEvent: TokenEvent): Boolean {
        return tokenEvent.isForce
    }
}
