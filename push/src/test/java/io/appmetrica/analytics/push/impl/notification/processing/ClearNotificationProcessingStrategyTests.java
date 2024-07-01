package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ClearNotificationProcessingStrategyTests {

    @Rule
    public final MockedStaticRule<PushMessageTrackerHub> sPushMessageTrackerHub =
        new MockedStaticRule<>(PushMessageTrackerHub.class);

    private ClearNotificationProcessingStrategy mStrategy;
    private Context mContext;
    private PushServiceProvider mPushServiceProvider;
    private AppMetricaPushCore mPushCore;
    private PushMessageTrackerHub mPushMessageTracker;
    private PushMessageHistory mPushMessageHistory;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mPushMessageTracker = mock(PushMessageTrackerHub.class);
        when(PushMessageTrackerHub.getInstance()).thenReturn(mPushMessageTracker);
        mPushCore = AppMetricaPushCore.getInstance(mContext);
        mPushServiceProvider = new MockablePushServiceProvider();
        mPushCore.setPushServiceProvider(mPushServiceProvider);
        mStrategy = new ClearNotificationProcessingStrategy();
        mPushServiceProvider.setAutoTrackingConfiguration(AutoTrackingConfiguration.newBuilder().build());
        mPushMessageHistory = mock(PushMessageHistory.class);
        mPushServiceProvider.setPushMessageHistory(mPushMessageHistory);
    }

    @Test
    public void testDoActionShouldReportClearNotificationToPushMessageTracker() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(NotificationActionInfo.newBuilder("").withPushId(randomString()).build())
        );
        verify(mPushMessageTracker, times(1))
            .onNotificationCleared(anyString(), nullable(String.class), anyString());
    }

    @Test
    public void testDoActionShouldNotReportClearNotificationToPushMessageTrackerIfPushIdIsNull() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(NotificationActionInfo.newBuilder("").withPushId(null).build())
        );
        verify(mPushMessageTracker, never()).onNotificationCleared(anyString(), anyString(), anyString());
    }

    @Test
    public void testDoActionShouldNotReportClearNotificationToPushMessageTrackerIfPushIdIsEmpty() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(NotificationActionInfo.newBuilder("").withPushId("").build())
        );
        verify(mPushMessageTracker, never()).onNotificationCleared(anyString(), anyString(), anyString());
    }

    @Test
    public void testDoActionShouldReportPushIdToPushMessageTracker() {
        String pushId = new RandomStringGenerator().nextString();
        mStrategy.doAction(
            mContext,
            wrapToIntent(NotificationActionInfo.newBuilder("").withPushId(pushId).build())
        );
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onNotificationCleared(arg.capture(), nullable(String.class), anyString());
        assertThat(arg.getValue()).isEqualTo(pushId);
    }

    @Test
    public void testSetPushActiveToFalse() {
        String pushId = randomString();
        NotificationActionInfo actionInfo = NotificationActionInfo.newBuilder("")
            .withPushId(pushId)
            .build();
        mStrategy.doAction(mContext, wrapToIntent(actionInfo));
        verify(mPushMessageHistory).setPushActive(eq(pushId), eq(false));
    }

    private String randomString() {
        return new RandomStringGenerator().nextString();
    }

    private Intent wrapToIntent(final NotificationActionInfo info) {
        return new Intent().putExtra(AppMetricaPush.EXTRA_ACTION_INFO, info);
    }
}
