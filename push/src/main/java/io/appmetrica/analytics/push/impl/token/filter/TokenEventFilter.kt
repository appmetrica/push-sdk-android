package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.event.TokenEvent

internal interface TokenEventFilter {

    val id: String

    fun shouldSend(tokenEvent: TokenEvent): Boolean
}
