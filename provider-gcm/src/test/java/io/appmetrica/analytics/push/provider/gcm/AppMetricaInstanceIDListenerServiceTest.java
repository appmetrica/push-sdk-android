package io.appmetrica.analytics.push.provider.gcm;

import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaInstanceIDListenerServiceTest {

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);
    @Rule
    public final MockedStaticRule<PushServiceFacade> pushServiceFacadeRule =
        new MockedStaticRule<>(PushServiceFacade.class);

    private AppMetricaInstanceIDListenerService appMetricaInstanceIDListenerService;
    private TrackersHub trackersHub;

    @Before
    public void setUp() {
        appMetricaInstanceIDListenerService = spy(AppMetricaInstanceIDListenerService.class);
        trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
    }

    @Test
    public void testOnTokenRefresh() {
        appMetricaInstanceIDListenerService.onTokenRefresh();

        verify(trackersHub, times(1)).reportEvent(anyString());

        pushServiceFacadeRule.getStaticMock().verify(() -> {
            PushServiceFacade.sendToken(appMetricaInstanceIDListenerService, CoreConstants.Transport.GCM, null);
        });
    }

    @Test
    public void testProcessToken() {
        appMetricaInstanceIDListenerService.processToken();

        verify(trackersHub, times(1)).reportEvent(anyString());

        pushServiceFacadeRule.getStaticMock().verify(() -> {
            PushServiceFacade.sendToken(appMetricaInstanceIDListenerService, CoreConstants.Transport.GCM, null);
        });
    }
}
