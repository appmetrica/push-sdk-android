package io.appmetrica.analytics.push.impl.token.event

import android.content.Context
import android.os.Bundle

interface TokenEventProvider {

    fun getTokenEvent(
        context: Context,
        bundle: Bundle
    ): TokenEvent?
}
