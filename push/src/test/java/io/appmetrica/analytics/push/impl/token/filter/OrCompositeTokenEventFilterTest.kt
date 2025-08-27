package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class OrCompositeTokenEventFilterTest : CommonTest() {

    private val filter1: TokenEventFilter = mock {
        on { shouldSend(any()) }.thenReturn(true)
    }
    private val filter2: TokenEventFilter = mock {
        on { shouldSend(any()) }.thenReturn(false)
    }
    private val filter3: TokenEventFilter = mock {
        on { shouldSend(any()) }.thenReturn(false)
    }

    @Test
    fun shouldSend() {
        val filter = OrCompositeTokenEventFilter(filter1, filter2)
        assertThat(filter.shouldSend(mock())).isTrue()
    }

    @Test
    fun shouldNotSend() {
        val filter = OrCompositeTokenEventFilter(filter2, filter3)
        assertThat(filter.shouldSend(mock())).isFalse()
    }
}
