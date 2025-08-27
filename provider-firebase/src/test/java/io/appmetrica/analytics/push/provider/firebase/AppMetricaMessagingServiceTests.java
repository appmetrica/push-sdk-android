package io.appmetrica.analytics.push.provider.firebase;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.messaging.RemoteMessage;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaMessagingServiceTests {

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);
    @Rule
    public final MockedStaticRule<PushServiceFacade> pushServiceFacadeRule =
        new MockedStaticRule<>(PushServiceFacade.class);

    private Context mContext;
    private AppMetricaMessagingService mAppMetricaMessagingService;
    private TrackersHub mTrackersHub;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mAppMetricaMessagingService = spy(AppMetricaMessagingService.class);
        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
    }

    @Test
    public void testOnMessageReceivedWithDataPayload() {
        String to = new RandomStringGenerator().nextString();
        String key = "yamp";
        String value = "data";
        RemoteMessage remoteMessage = new RemoteMessage.Builder(to).addData(key, value).build();

        mAppMetricaMessagingService.onMessageReceived(remoteMessage);

        ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);
        verify(mAppMetricaMessagingService, times(1))
            .processPush(any(Context.class), dataArg.capture());
        assertThat(dataArg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void testOnMessageReceivedWithoutDataPayload() {
        String to = new RandomStringGenerator().nextString();
        RemoteMessage remoteMessage = new RemoteMessage.Builder(to).build();

        mAppMetricaMessagingService.onMessageReceived(remoteMessage);

        ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);
        verify(mAppMetricaMessagingService, times(1))
            .processPush(any(Context.class), dataArg.capture());
        assertThat(dataArg.getValue().isEmpty()).isEqualTo(true);
    }

    @Test
    public void testProcessPushWithRemoteMessage() {
        String to = new RandomStringGenerator().nextString();
        String key = "yamp";
        String value = "{\"data\": \"value\"}";
        RemoteMessage remoteMessage = new RemoteMessage.Builder(to).addData(key, value).build();
        mAppMetricaMessagingService.processPush(mContext, remoteMessage);

        verify(mTrackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        pushServiceFacadeRule.getStaticMock().verify(() -> {
            PushServiceFacade.processPush(eq(mContext), arg.capture(), eq(CoreConstants.Transport.FIREBASE));
        });
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
        pushServiceFacadeRule.getStaticMock().verify(() -> {
            PushServiceFacade.processPush(eq(mContext), arg.capture(), eq(CoreConstants.Transport.FIREBASE));
        });
        assertThat(arg.getValue().getString(key)).isEqualTo(value);

    }

    @Test
    public void testOnTokenRefresh() {
        String token = "Some new token";
        TrackersHub trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);

        mAppMetricaMessagingService.onNewToken(token);

        verify(trackersHub, times(1)).reportEvent(anyString());

        pushServiceFacadeRule.getStaticMock().verify(() -> {
            PushServiceFacade.sendTokenOnRefresh(mAppMetricaMessagingService, CoreConstants.Transport.FIREBASE, token);
        });
    }

    @Test
    public void testProcessToken() {
        String token = "token";
        TrackersHub trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);

        mAppMetricaMessagingService.processToken(mContext, token);

        verify(trackersHub).reportEvent(anyString());

        pushServiceFacadeRule.getStaticMock().verify(() -> {
            PushServiceFacade.sendTokenManually(mContext, CoreConstants.Transport.FIREBASE, token);
        });
    }
}
