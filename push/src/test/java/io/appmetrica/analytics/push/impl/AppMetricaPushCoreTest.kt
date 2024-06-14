package io.appmetrica.analytics.push.impl

import android.content.Context
import io.appmetrica.analytics.push.TokenUpdateListener
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.impl.utils.AppMetricaTracker
import io.appmetrica.analytics.push.settings.PushMessageTracker
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.constructionRule
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class AppMetricaPushCoreTest : CommonTest() {

    private val context: Context = mock()

    private val tokens = mapOf(
        CoreConstants.Transport.FIREBASE to CoreConstants.Transport.FIREBASE,
        CoreConstants.Transport.GCM to CoreConstants.Transport.GCM,
        CoreConstants.Transport.HMS to CoreConstants.Transport.HMS
    )

    private val trackersHub: TrackersHub = mock()

    @get:Rule
    val trackersHubMockedStaticRule = staticRule<TrackersHub> {
        on { TrackersHub.getInstance() } doReturn trackersHub
    }

    private val pushMessageTrackerHub: PushMessageTrackerHub = mock()

    @get:Rule
    val pushMessageTrackerHubMockedStaticRule = staticRule<PushMessageTrackerHub> {
        on { PushMessageTrackerHub.getInstance() } doReturn pushMessageTrackerHub
    }

    private val preferenceManager: PreferenceManager = mock()
    private val pushMessageTracker: PushMessageTracker = mock()

    @get:Rule
    val appMetricaPushServiceProviderMockedConstructionRule = constructionRule<AppMetricaPushServiceProvider> {
        on { preferenceManager } doReturn preferenceManager
        on { pushMessageTracker } doReturn pushMessageTracker
    }

    @get:Rule
    val appMetricaTrackerMockedConstructionRule = constructionRule<AppMetricaTracker>()

    private val core by setUp { AppMetricaPushCore(context) }

    @Test
    fun appmetricaPushServiceProvider() {
        assertThat(appMetricaPushServiceProviderMockedConstructionRule.constructionMock.constructed())
            .hasSize(1)
        assertThat(appMetricaPushServiceProviderMockedConstructionRule.argumentInterceptor.flatArguments())
            .containsExactly(context, core)
    }

    @Test
    fun appMetricaTracker() {
        verify(trackersHub)
            .registerTracker(appMetricaTrackerMockedConstructionRule.constructionMock.constructed().first())
        assertThat(appMetricaTrackerMockedConstructionRule.constructionMock.constructed()).hasSize(1)
        assertThat(appMetricaTrackerMockedConstructionRule.argumentInterceptor.flatArguments())
            .containsExactly(context, "0e5e9c33-f8c3-4568-86c5-2e4f57523f72", preferenceManager)
    }

    @Test
    fun pushMessageTracker() {
        verify(pushMessageTrackerHub).registerTracker(pushMessageTracker)
    }

    @Test
    fun updateTokens() {
        core.updateTokens(tokens)
        assertThat(core.tokens).isEqualTo(tokens)
    }

    @Test
    fun callTokenUpdateListener() {
        val listener = Mockito.mock(TokenUpdateListener::class.java)
        core.setTokenUpdateListener(listener)
        core.updateTokens(tokens)
        verify(listener).onTokenUpdated(tokens)
    }

    @Test
    fun getPreferenceManager() {
        assertThat(core.preferenceManager).isEqualTo(preferenceManager)
    }
}
