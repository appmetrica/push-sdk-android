package io.appmetrica.analytics.push.provider.gcm;

import android.content.Context;
import android.os.Bundle;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaInstanceIDListenerServiceTest {

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private AppMetricaInstanceIDListenerService appMetricaInstanceIDListenerService;
    private TrackersHub trackersHub;
    private PushServiceFacade.CommandServiceWrapper commandServiceWrapper;

    @Before
    public void setUp() {
        appMetricaInstanceIDListenerService = spy(AppMetricaInstanceIDListenerService.class);
        trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        commandServiceWrapper = mock(PushServiceFacade.CommandServiceWrapper.class);
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper);
    }

    @Test
    public void testOnTokenRefresh() {
        appMetricaInstanceIDListenerService.onTokenRefresh();

        verify(trackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_UPDATE_TOKEN);
    }

    @Test
    public void testProcessToken() {
        appMetricaInstanceIDListenerService.processToken();

        verify(trackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_UPDATE_TOKEN);
    }
}
