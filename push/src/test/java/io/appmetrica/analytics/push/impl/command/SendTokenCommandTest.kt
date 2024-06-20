package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SendTokenCommandTest : CommonTest() {

    private val transport = "firebase"

    private val context: Context = mock()

    private val bundle = Bundle().apply {
        putString(CoreConstants.EXTRA_TRANSPORT, transport)
    }

    private val core: AppMetricaPushCore = mock()

    @get:Rule
    val coreMockedStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn core
    }

    private val sendTokenCommand: SendTokenCommand by setUp { SendTokenCommand() }

    @Test
    fun `execute without token`() {
        sendTokenCommand.execute(context, bundle)
        verifyNoInteractions(core)
    }

    @Test
    fun `execute with token`() {
        val token = "some push token"
        bundle.putString(PushServiceFacade.TOKEN, token)
        sendTokenCommand.execute(context, bundle)

        verify(core).onTokenUpdated(mapOf(transport to token), null)
    }
}
