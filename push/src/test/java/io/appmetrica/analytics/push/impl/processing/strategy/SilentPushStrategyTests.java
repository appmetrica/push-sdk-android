package io.appmetrica.analytics.push.impl.processing.strategy;

import android.content.Context;
import android.content.Intent;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SilentPushStrategyTests {

    @Rule
    public final MockedStaticRule<PushMessageTrackerHub> sPushMessageTrackerHub =
        new MockedStaticRule<>(PushMessageTrackerHub.class);

    private Context mContext;
    private SilentPushStrategy mStrategy;
    private PushMessage mPushMessage;
    private PushServiceProvider mPushServiceProvider;
    private PushMessageTrackerHub mPushMessageTracker;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        mStrategy = new SilentPushStrategy();
        mPushMessage = mock(PushMessage.class);
        final AppMetricaPushCore pushCore = AppMetricaPushCore.getInstance(mContext);
        mPushServiceProvider = new MockablePushServiceProvider();
        pushCore.setPushServiceProvider(mPushServiceProvider);
        mPushServiceProvider.setAutoTrackingConfiguration(AutoTrackingConfiguration.newBuilder().build());
        mPushMessageTracker = mock(PushMessageTrackerHub.class);
        when(PushMessageTrackerHub.getInstance()).thenReturn(mPushMessageTracker);
    }

    @Test
    public void testProcessSilentPushShouldSendBroadcastWithExpectedAction() {
        mStrategy.processPush(mContext, mPushMessage);
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).sendBroadcast(arg.capture());
        assertThat(arg.getValue().getAction()).contains(mContext.getPackageName());
        assertThat(arg.getValue().getAction()).contains(Constants.SILENT_PUSH_POSTFIX);
    }

    @Test
    public void testProcessSilentPushShouldSendPayloadInBroadcast() {
        String payload = new RandomStringGenerator().nextString();
        when(mPushMessage.getPayload()).thenReturn(payload);
        mStrategy.processPush(mContext, mPushMessage);
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).sendBroadcast(arg.capture());
        assertThat(arg.getValue().getStringExtra(AppMetricaPush.EXTRA_PAYLOAD)).isEqualTo(payload);
    }

    @Test
    public void testProcessSilentPushShouldSendSilentPushProcessedToPushMessageTracker() {
        String notificationId = new RandomStringGenerator().nextString();
        when(mPushMessage.getNotificationId()).thenReturn(notificationId);
        mStrategy.processPush(mContext, mPushMessage);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onSilentPushProcessed(arg.capture(), nullable(String.class), nullable(String.class));
        assertThat(arg.getValue()).isEqualTo(notificationId);
    }

    @Test
    public void testProcessSilentPushShouldNotSendSilentPushProcessedToPushMessageTrackerIfPushIdIsEmpty() {
        mStrategy.processPush(mContext, mPushMessage);
        verify(mPushMessageTracker, never()).onSilentPushProcessed(anyString(), anyString(), anyString());
    }
}
