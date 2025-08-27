package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.event.TokenEvent

interface TokenEventFilter {

    val id: String

    fun shouldSend(tokenEvent: TokenEvent): Boolean
}
