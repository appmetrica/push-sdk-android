package io.appmetrica.analytics.push.impl.token.event

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.assertions.ObjectPropertyAssertions
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.commands.PushTokenCommandInfo
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DefaultTokenEventProviderTest : CommonTest() {

    private val token = "some_token"
    private val provider = "some_provider"
    private val force = true

    private val context: Context = mock()
    private val pushServiceController: PushServiceControllerComposite = mock()
    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { pushServiceController } doReturn pushServiceController
    }
    @get:Rule
    val appMetricaPushCoreRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val trackersHub: TrackersHub = mock()
    @get:Rule
    val trackersHubRule = staticRule<TrackersHub> {
        on { TrackersHub.getInstance() } doReturn trackersHub
    }

    private val tokenEventProvider = DefaultTokenEventProvider()

    @Test
    fun getTokenEventWithTokenFromBundle() {
        val bundle = Bundle().apply {
            putBundle(
                Commands.SendPushToken.EXTRA_INFO,
                PushTokenCommandInfo.Builder(provider)
                    .withToken(token)
                    .withForce(force)
                    .build()
                    .toBundle()
            )
        }

        val result = tokenEventProvider.getTokenEvent(context, bundle)

        ObjectPropertyAssertions(result)
            .withIgnoredFields("tokenKey")
            .checkField("token", token)
            .checkField("provider", provider)
            .checkField("isForce", force)
            .checkAll()
    }

    @Test
    fun getTokenEventWithTokenFromController() {
        val bundle = Bundle().apply {
            putBundle(
                Commands.SendPushToken.EXTRA_INFO,
                PushTokenCommandInfo.Builder(provider)
                    .withForce(force)
                    .build()
                    .toBundle()
            )
        }

        val newToken = "some_new_token"
        whenever(pushServiceController.getToken(provider)).thenReturn(newToken)

        val result = tokenEventProvider.getTokenEvent(context, bundle)

        ObjectPropertyAssertions(result)
            .withIgnoredFields("tokenKey")
            .checkField("token", newToken)
            .checkField("provider", provider)
            .checkField("isForce", force)
            .checkAll()
    }

    @Test
    fun getTokenEventWithoutCommandInfo() {
        val bundle = Bundle().apply {
            putString("key1", "value1")
            putBundle(
                "bundle_key",
                Bundle().apply {
                    putString("key2", "value2")
                }
            )
        }
        val result = tokenEventProvider.getTokenEvent(context, bundle)

        assertThat(result).isNull()
        verify(trackersHub).reportEvent(
            "Failed to get token event from bundle Bundle[{key1=value1, bundle_key=Bundle[{key2=value2}]}]"
        )
    }
}
