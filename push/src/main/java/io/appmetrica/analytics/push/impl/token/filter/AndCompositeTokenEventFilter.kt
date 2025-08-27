package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.logger.internal.PublicLogger

class AndCompositeTokenEventFilter(
    private vararg val filters: TokenEventFilter
) : TokenEventFilter {

    override val id = "ALL${filters.map { it.id }}"

    override fun shouldSend(tokenEvent: TokenEvent): Boolean {
        return filters.all { filter ->
            filter.shouldSend(tokenEvent).also {
                PublicLogger.info(
                    "${filter.id} provider: ${tokenEvent.provider}, shouldSend: $it"
                )
            }
        }
    }
}
