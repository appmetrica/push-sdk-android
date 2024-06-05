package io.appmetrica.analytics.push.coreutils.internal;

import android.content.Context;
import android.os.Bundle;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(RobolectricTestRunner.class)
public class PushServiceFacadeTest {

    private Context context;
    private PushServiceFacade.CommandServiceWrapper commandServiceWrapper;
    private Bundle pushMessageBundle = new Bundle();

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        commandServiceWrapper = mock(PushServiceFacade.CommandServiceWrapper.class);
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper);
        pushMessageBundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, new JSONObject().toString());
    }

    @Test
    public void testInitTokenShouldSendIntentWithExpectedCommand() {
        PushServiceFacade.initToken(context);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_INIT_PUSH_TOKEN);
    }

    @Test
    public void testRefreshTokenShouldSendIntentWithExpectedCommand() {
        PushServiceFacade.refreshToken(context);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_UPDATE_TOKEN);
    }

    @Test
    public void testProcessPushShouldSendIntentWithExpectedCommand() {
        String transport = "my_transport";
        PushServiceFacade.processPush(context, pushMessageBundle, transport);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1))
            .startCommand(any(Context.class), arg.capture(), anyBoolean());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_PROCESS_PUSH);
        assertThat(arg.getValue().getString(CoreConstants.EXTRA_TRANSPORT)).isEqualTo(transport);
    }

    @Test
    public void testInitPushServiceShouldSendIntentWithExpectedCommand() {
        PushServiceFacade.initPushService(context);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().getString(PushServiceFacade.EXTRA_COMMAND))
            .isEqualTo(PushServiceFacade.COMMAND_INIT_PUSH_SERVICE);
    }

    @Test
    public void testInitPushServiceShouldSendReceivedTime() {
        PushServiceFacade.initPushService(context);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testInitTokenShouldSendReceivedTime() {
        PushServiceFacade.initToken(context);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testInitTokenWithForceShouldSendReceivedTime() {
        PushServiceFacade.initToken(context, true);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testRefreshTokenShouldSendReceivedTime() {
        PushServiceFacade.refreshToken(context);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testRefreshTokenWithForceShouldSendReceivedTime() {
        PushServiceFacade.refreshToken(context, true);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testRefreshTokenWithRefreshTokenInfoShouldSendReceivedTime() {
        PushServiceFacade.refreshToken(context, new RefreshTokenInfo(false));
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1)).startCommand(any(Context.class), arg.capture());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testProcessPushInfoShouldSendReceivedTime() {
        PushServiceFacade.processPush(context, pushMessageBundle, "transport");
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper, times(1))
            .startCommand(any(Context.class), arg.capture(), anyBoolean());
        assertThat(arg.getValue().containsKey(PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME)).isTrue();
    }

    @Test
    public void testProcessPushInfoShouldNotStartCommandIfNotOwnPush() {
        PushServiceFacade.processPush(context, new Bundle(), "transport");
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verifyNoInteractions(commandServiceWrapper);
    }

    @Test
    public void processPushWithMinProcessingDelay() throws Exception {
        long value = 13;
        JSONObject root = new JSONObject();
        root.put(CoreConstants.PushMessage.PROCESSING_MIN_TIME, value);
        pushMessageBundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, root.toString());
        PushServiceFacade.processPush(context, pushMessageBundle, "Some transport");
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper).startCommand(any(Context.class), arg.capture(), anyBoolean());
        assertThat(arg.getValue().getLong(CoreConstants.MIN_PROCESSING_DELAY, -1))
            .isEqualTo(value);
    }

    @Test
    public void processPushWithoutMinProcessingDelay() throws Exception {
        PushServiceFacade.processPush(context, pushMessageBundle, "Some transport");
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(commandServiceWrapper).startCommand(any(Context.class), arg.capture(), anyBoolean());
        assertThat(arg.getValue().getLong(CoreConstants.MIN_PROCESSING_DELAY, -1)).isEqualTo(-1);
    }
}
