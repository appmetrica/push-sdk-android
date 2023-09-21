package io.appmetrica.analytics.push.impl.notification.processing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.RemoteInput;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.settings.PushMessageTracker;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Random;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static io.appmetrica.analytics.push.impl.notification.processing.InlineActionProcessingStrategy.KEY_TEXT_REPLY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class InlineActionProcessingStrategyTests {

    @Rule
    public final MockedStaticRule<RemoteInput> sRemoteInput = new MockedStaticRule<>(RemoteInput.class);

    private Context mContext;
    private InlineActionProcessingStrategy mStrategy;
    private PushServiceProvider mPushServiceProvider;
    private AppMetricaPushCore mPushCore;
    private PushMessageTracker mPushMessageTracker;
    private Bundle mRemoteInputBundle;
    private NotificationManager mNotificationManager;
    private PushMessageHistory mPushMessageHistory;

    private static final String TRANSPORT = "transport";

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        mPushCore = AppMetricaPushCore.getInstance(mContext);
        mPushServiceProvider = new MockablePushServiceProvider();
        mPushCore.setPushServiceProvider(mPushServiceProvider);
        mPushMessageTracker = mPushServiceProvider.getPushMessageTracker();
        mStrategy = new InlineActionProcessingStrategy();
        mPushServiceProvider.setAutoTrackingConfiguration(AutoTrackingConfiguration.newBuilder().build());
        mRemoteInputBundle = mock(Bundle.class);
        when(RemoteInput.getResultsFromIntent(any(Intent.class))).thenReturn(mRemoteInputBundle);
        mNotificationManager = mock(NotificationManager.class);
        mPushMessageHistory = mock(PushMessageHistory.class);
        mPushServiceProvider.setPushMessageHistory(mPushMessageHistory);
    }

    @Test
    public void testDoActionShouldReportInlineAdditionalActionToPushMessageTracker() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString()))
            .thenReturn("");
        mStrategy.doAction(mContext, wrapToIntent(
            NotificationActionInfo.newBuilder(TRANSPORT)
                .withPushId(randomString())
                .build())
        );
        verify(mPushMessageTracker, times(1))
            .onNotificationInlineAdditionalAction(
                anyString(),
                nullable(String.class),
                nullable(String.class),
                anyString(),
                eq(TRANSPORT)
            );
    }

    @Test
    public void testDoActionShouldNotReportInlineAdditionalActionToPushMessageTrackerIfPushIdIsNull() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
        mStrategy.doAction(mContext, wrapToIntent(
            NotificationActionInfo.newBuilder(TRANSPORT)
                .withPushId(null)
                .build())
        );
        verify(mPushMessageTracker, never())
            .onNotificationInlineAdditionalAction(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                eq(TRANSPORT)
            );
    }

    @Test
    public void testDoActionShouldNotReportInlineAdditionalActionToPushMessageTrackerIfPushIdIsEmpty() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
        mStrategy.doAction(mContext, wrapToIntent(
            NotificationActionInfo.newBuilder(TRANSPORT)
                .withPushId("")
                .build())
        );
        verify(mPushMessageTracker, never())
            .onNotificationInlineAdditionalAction(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                eq(TRANSPORT)
            );
    }

    @Test
    public void testDoActionShouldReportPushIdToTracker() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
        String pushId = randomString();
        mStrategy.doAction(mContext, wrapToIntent(
            NotificationActionInfo.newBuilder(TRANSPORT)
                .withPushId(pushId)
                .build())
        );
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onNotificationInlineAdditionalAction(
                arg.capture(),
                nullable(String.class),
                nullable(String.class),
                anyString(),
                eq(TRANSPORT)
            );
        assertThat(arg.getValue()).isEqualTo(pushId);
    }

    @Test
    public void testDoActionShouldReportActionIdToTracker() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
        String actionId = randomString();
        mStrategy.doAction(mContext, wrapToIntent(
            NotificationActionInfo.newBuilder(TRANSPORT)
                .withPushId(randomString())
                .withActionId(actionId)
                .build())
        );
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onNotificationInlineAdditionalAction(
                anyString(),
                arg.capture(),
                nullable(String.class),
                anyString(),
                eq(TRANSPORT)
            );
        assertThat(arg.getValue()).isEqualTo(actionId);
    }

    @Test
    public void testDoActionShouldReportTextToTracker() {
        String text = randomString();
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn(text);
        mStrategy.doAction(mContext, wrapToIntent(
            NotificationActionInfo.newBuilder(TRANSPORT)
                .withPushId(randomString())
                .build())
        );
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onNotificationInlineAdditionalAction(
                anyString(),
                nullable(String.class),
                nullable(String.class),
                arg.capture(),
                eq(TRANSPORT)
            );
        assertThat(arg.getValue()).isEqualTo(text);
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void testDoActionShouldClearNotificationIfApiNotArchived() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
        String notificationTag = randomString();
        int notificationId = new Random().nextInt();
        NotificationActionInfo actionInfo = NotificationActionInfo.newBuilder(TRANSPORT)
            .withNotificationTag(notificationTag)
            .withNotificationId(notificationId)
            .build();
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        mStrategy.doAction(mContext, wrapToIntent(actionInfo));
        verify(mNotificationManager).cancel(eq(notificationTag), eq(notificationId));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.P)
    public void testDoActionShouldClearNotificationIfApiArchived() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
        String notificationTag = randomString();
        int notificationId = new Random().nextInt();
        NotificationActionInfo actionInfo = NotificationActionInfo.newBuilder(TRANSPORT)
            .withNotificationTag(notificationTag)
            .withNotificationId(notificationId)
            .build();
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        mStrategy.doAction(mContext, wrapToIntent(actionInfo));
        verify(mNotificationManager).notify(eq(notificationTag), eq(notificationId), any(Notification.class));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void testSetPushActiveToFalse() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn("");
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
