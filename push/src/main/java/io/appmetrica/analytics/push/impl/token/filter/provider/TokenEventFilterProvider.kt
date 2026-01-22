package io.appmetrica.analytics.push.impl.token.filter.provider

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.impl.token.filter.TokenEventFilter

internal interface TokenEventFilterProvider {

    fun getTokenEventFilter(
        context: Context,
        bundle: Bundle
    ): TokenEventFilter
}
