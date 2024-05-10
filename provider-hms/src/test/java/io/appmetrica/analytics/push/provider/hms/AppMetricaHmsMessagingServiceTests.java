package io.appmetrica.analytics.push.provider.hms;

import android.content.Context;
import android.os.Bundle;
import com.huawei.hms.push.RemoteMessage;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaHmsMessagingServiceTests {

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private Context mContext;
    private AppMetricaHmsMessagingService mAppMetricaHmsMessagingService;
    private TrackersHub mTrackersHub;
    private PushServiceFacade.CommandServiceWrapper commandServiceWrapper;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mAppMetricaHmsMessagingService = spy(AppMetricaHmsMessagingService.class);
        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
        commandServiceWrapper = mock(PushServiceFacade.CommandServiceWrapper.class);
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper);
    }

    @Test
    public void testOnMessageReceivedWithDataPayload() {
        String to = new RandomStringGenerator().nextString();
        String key = "yamp";
        String value = "data";
        RemoteMessage remoteMessage = new RemoteMessage.Builder(to).addData(key, value).build();

        mAppMetricaHmsMessagingService.onMessageReceived(remoteMessage);

        ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);
        verify(mAppMetricaHmsMessagingService, times(1)).processPush(any(Context.class), dataArg.capture());
        assertThat(dataArg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void testOnMessageReceivedWithoutDataPayload() {
        String to = new RandomStringGenerator().nextString();
        RemoteMessage remoteMessage = new RemoteMessage.Builder(to).build();

        mAppMetricaHmsMessagingService.onMessageReceived(remoteMessage);

        ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);
        verify(mAppMetricaHmsMessagingService, times(1)).processPush(any(Context.class), dataArg.capture());
        assertThat(dataArg.getValue().isEmpty()).isEqualTo(true);
    }

    @Test
    public void testProcessPushWithRemoteMessage() {
        String to = new RandomStringGenerator().nextString();
        String key = "yamp";
        String value = "{\"data\": \"value\"}";
        RemoteMessage remoteMessage = new RemoteMessage.Builder(to).addData(key, value).build();
        mAppMetricaHmsMessagingService.processPush(mContext, remoteMessage);

        verify(mTrackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1))
            .startCommand(any(Context.class), arg.capture(), anyBoolean());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_PROCESS_PUSH);
        assertThat(arg.getValue().getString(CoreConstants.EXTRA_TRANSPORT)).isEqualTo(CoreConstants.Transport.HMS);
        assertThat(arg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void testProcessPushWithBundle() {
        Bundle data = new Bundle();
        String key = "yamp";
        String value = new JSONObject().toString();
        data.putString(key, value);
        mAppMetricaHmsMessagingService.processPush(mContext, data);

        verify(mTrackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1))
            .startCommand(any(Context.class), arg.capture(), anyBoolean());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_PROCESS_PUSH);
        assertThat(arg.getValue().getString(CoreConstants.EXTRA_TRANSPORT)).isEqualTo(CoreConstants.Transport.HMS);
        assertThat(arg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void testOnTokenRefresh() {
        TrackersHub trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        PushServiceFacade.CommandServiceWrapper commandServiceWrapper =
            mock(PushServiceFacade.CommandServiceWrapper.class);
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper);

        AppMetricaHmsMessagingService appMetricaHmsMessagingService = spy(AppMetricaHmsMessagingService.class);
        appMetricaHmsMessagingService.onNewToken("");

        verify(trackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_UPDATE_TOKEN);
    }

    @Test
    public void testProcessToken() {
        TrackersHub trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        PushServiceFacade.CommandServiceWrapper commandServiceWrapper =
            mock(PushServiceFacade.CommandServiceWrapper.class);
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper);

        AppMetricaHmsMessagingService appMetricaHmsMessagingService = spy(AppMetricaHmsMessagingService.class);
        appMetricaHmsMessagingService.processToken(appMetricaHmsMessagingService, "");

        verify(trackersHub, times(1)).reportEvent(anyString());

        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_UPDATE_TOKEN);
    }

    @Test
    public void regularMessage() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("a", "a");
        object.put("b", "b");
        Bundle bundle = mAppMetricaHmsMessagingService.jsonToBundle(object.toString());
        assertThat(bundle.keySet()).containsOnly("a", "b");
        assertThat(bundle.getString("a")).isEqualTo("a");
        assertThat(bundle.getString("b")).isEqualTo("b");
    }

    @Test
    public void nullMessage() {
        Bundle bundle = mAppMetricaHmsMessagingService.jsonToBundle(null);
        assertThat(bundle.isEmpty()).isTrue();
    }

    @Test
    public void emptyMessage() {
        Bundle bundle = mAppMetricaHmsMessagingService.jsonToBundle("");
        assertThat(bundle.isEmpty()).isTrue();
    }

    @Test
    public void messageWithInt() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("a", "a");
        object.put("b", 1);
        Bundle bundle = mAppMetricaHmsMessagingService.jsonToBundle(object.toString());
        assertThat(bundle.keySet()).containsOnly("a", "b");
        assertThat(bundle.getString("a")).isEqualTo("a");
        assertThat(bundle.getString("b")).isEqualTo("1");
    }

    @Test
    public void messageWithAnotherJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("a", "a");
        object.put("b", new JSONObject().put("c", "c"));
        Bundle bundle = mAppMetricaHmsMessagingService.jsonToBundle(object.toString());
        assertThat(bundle.keySet()).containsOnly("a", "b");
        assertThat(bundle.getString("a")).isEqualTo("a");
        assertThat(bundle.getString("b")).isEqualTo("{\"c\":\"c\"}");
    }
}
