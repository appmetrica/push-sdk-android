package io.appmetrica.analytics.push.impl.token.event

import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TokenEventTest : CommonTest() {

    @Test
    fun toJsonWithNullToken() {
        val event = TokenEvent.Builder("provider").build()
        val result = event.toJson()
        assertThat(result.toString()).isEqualTo("{}")
    }

    @Test
    fun toJson() {
        val event = TokenEvent.Builder("provider")
            .withToken("some_token")
            .build()
        val result = event.toJson()

        assertThat(result.toString()).isEqualTo("{\"token\":\"some_token\"}")
    }
}
