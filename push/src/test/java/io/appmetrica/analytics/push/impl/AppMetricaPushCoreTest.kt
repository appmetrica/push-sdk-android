package io.appmetrica.analytics.push.impl

import android.content.Context
import io.appmetrica.analytics.ModulesFacade
import io.appmetrica.analytics.push.TokenUpdateListener
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.event.PushEvent
import io.appmetrica.analytics.push.impl.event.InternalPushMessageTrackerWrapper
import io.appmetrica.analytics.push.impl.notification.NotificationStatus
import io.appmetrica.analytics.push.impl.notification.NotificationStatusProvider
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.impl.utils.AppMetricaTracker
import io.appmetrica.analytics.push.impl.utils.MainProcessDetector
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.constructionRule
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

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
    private val pushMessageTracker: InternalPushMessageTracker = mock()
    private val mainProcessDetector: MainProcessDetector = mock {
        on { isMainProcess } doReturn true
    }
    private val notificationStatus: NotificationStatus = mock()
    private val notificationStatusProvider: NotificationStatusProvider = mock {
        on { notificationStatus } doReturn notificationStatus
    }

    @get:Rule
    val appMetricaPushServiceProviderMockedConstructionRule = constructionRule<AppMetricaPushServiceProvider> {
        on { preferenceManager } doReturn preferenceManager
        on { pushMessageTracker } doReturn pushMessageTracker
        on { mainProcessDetector } doReturn mainProcessDetector
        on { notificationStatusProvider } doReturn notificationStatusProvider
    }

    @get:Rule
    val pushServiceControllerCompositeRule = constructionRule<PushServiceControllerComposite>()

    @get:Rule
    val appMetricaTrackerMockedConstructionRule = constructionRule<AppMetricaTracker>()

    @get:Rule
    val modulesFacadeRule = staticRule<ModulesFacade> {
        on { ModulesFacade.isActivatedForApp() } doReturn true
    }

    private val pushMessageTrackerWrapper: InternalPushMessageTrackerWrapper = mock()

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
    fun setTokenUpdateListener() {
        val listener = mock<TokenUpdateListener>()
        core.setTokenUpdateListener(listener)
        assertThat(core.tokenUpdateListener).isEqualTo(listener)
    }

    @Test
    fun getPreferenceManager() {
        assertThat(core.preferenceManager).isEqualTo(preferenceManager)
    }

    @Test
    fun reportPushEvent() {
        core.setPushMessageTracker(pushMessageTrackerWrapper)
        val pushEvent: PushEvent = mock()
        core.reportPushEvent(pushEvent)

        verify(pushMessageTrackerWrapper).reportPushEvent(pushEvent)
    }
}
