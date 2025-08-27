package io.appmetrica.analytics.push.provider.gcm;

import android.content.Context;
import android.os.Bundle;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaGcmListenerServiceTest {

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);
    @Rule
    public final MockedStaticRule<PushServiceFacade> pushServiceFacadeRule =
        new MockedStaticRule<>(PushServiceFacade.class);

    private Context mContext;
    private AppMetricaGcmListenerService mAppMetricaMessagingService;
    private TrackersHub mTrackersHub;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mAppMetricaMessagingService = spy(AppMetricaGcmListenerService.class);
        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
    }

    @Test
    public void testOnMessageReceivedWithBundle() {
        Bundle data = new Bundle();
        String key = "yamp";
        String value = new JSONObject().toString();
        data.putString(key, value);
        mAppMetricaMessagingService.onMessageReceived("", data);

        verify(mTrackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        pushServiceFacadeRule.getStaticMock().verify(() -> PushServiceFacade.processPush(
            eq(mAppMetricaMessagingService),
            arg.capture(),
            eq(CoreConstants.Transport.GCM)
        ));
        assertThat(arg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void testProcessPushWithBundle() {
        Bundle data = new Bundle();
        String key = "yamp";
        String value = new JSONObject().toString();
        data.putString(key, value);
        mAppMetricaMessagingService.processPush(mContext, data);

        verify(mTrackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        pushServiceFacadeRule.getStaticMock().verify(() -> PushServiceFacade.processPush(
            eq(mContext),
            arg.capture(),
            eq(CoreConstants.Transport.GCM)
        ));
        assertThat(arg.getValue().getString(key)).isEqualTo(value);
    }
}
