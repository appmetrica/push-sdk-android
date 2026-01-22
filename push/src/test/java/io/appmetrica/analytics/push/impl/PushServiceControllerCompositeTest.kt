package io.appmetrica.analytics.push.impl

import android.content.Context
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.provider.api.PushServiceController
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PushServiceControllerCompositeTest : CommonTest() {

    @get:Rule
    val pushServiceFacade = staticRule<PushServiceFacade>()

    private val transport1 = "transport#1"
    private val transport2 = "transport2"

    private val context: Context = mock()
    private val controller1: PushServiceController = mock {
        on { transportId } doReturn transport1
    }
    private val controller2: PushServiceController = mock {
        on { transportId } doReturn transport2
    }

    private var controller: PushServiceControllerComposite = PushServiceControllerComposite(
        context,
        listOf(controller1, controller2)
    )

    @Test
    fun registerCall() {
        controller.register()
        verify(controller1).register()
        verify(controller2).register()
    }

    @Test
    fun callIfSmthRegistered() {
        whenever(controller1.register()).thenReturn(true)
        controller.register()
        pushServiceFacade.staticMock.verify {
            PushServiceFacade.initToken(context, controller1.transportId)
        }
    }

    @Test
    fun doNotCallIfNothingRegistered() {
        whenever(controller1.register()).thenReturn(false)
        whenever(controller2.register()).thenReturn(false)
        controller.register()
        pushServiceFacade.staticMock.verify({
            PushServiceFacade.initToken(eq(context), any())
        }, never())
    }

    @Test
    fun getAllTokens() {
        val token1 = "token1"
        whenever(controller1.token).thenReturn(token1)
        val token2 = "token2"
        whenever(controller2.token).thenReturn(token2)

        assertThat(controller.tokens).containsExactlyEntriesOf(
            mapOf(
                transport1 to token1,
                transport2 to token2
            )
        )
    }

    @Test
    fun getEmptyToken() {
        val token1 = "token1"
        whenever(controller1.token).thenReturn(token1)
        whenever(controller2.token).thenReturn(null)

        assertThat(controller.tokens).containsExactlyEntriesOf(
            mapOf(
                transport1 to token1,
                transport2 to null
            )
        )
    }

    @Test
    fun shouldSendTokenForProvider() {
        val token1 = "token1"
        whenever(controller1.token).thenReturn(token1)
        val token2 = "token2"
        whenever(controller2.token).thenReturn(token2)
        whenever(controller1.shouldSendToken(token1)).thenReturn(true)
        whenever(controller2.shouldSendToken(token2)).thenReturn(false)

        assertThat(controller.shouldSendTokenForProvider(token1, transport1)).isTrue()
        assertThat(controller.shouldSendTokenForProvider(token2, transport2)).isFalse()
    }

    @Test
    fun getToken() {
        val token = "some token"
        whenever(controller1.token).thenReturn(token)

        assertThat(controller.getToken(transport1)).isEqualTo(token)
    }
}
