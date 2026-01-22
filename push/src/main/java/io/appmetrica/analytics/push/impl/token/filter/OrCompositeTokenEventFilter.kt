package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.logger.internal.PublicLogger

internal class OrCompositeTokenEventFilter(
    private vararg val filters: TokenEventFilter
) : TokenEventFilter {

    override val id = "ANY${filters.map { it.id }}"

    override fun shouldSend(tokenEvent: TokenEvent): Boolean {
        return filters.any { filter ->
            filter.shouldSend(tokenEvent).also {
                PublicLogger.info(
                    "${filter.id} provider: ${tokenEvent.provider}, shouldSend: $it"
                )
            }
        }
    }
}
