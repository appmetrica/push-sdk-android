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
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Random;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static io.appmetrica.analytics.push.impl.notification.processing.InlineActionProcessingStrategy.KEY_TEXT_REPLY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
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
    private InternalPushMessageTracker mPushMessageTracker;
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
    public void doActionShouldReportInlineAdditionalActionToPushMessageTracker() {
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString()))
            .thenReturn("");
        NotificationActionInfo notificationActionInfo = NotificationActionInfo.newBuilder(TRANSPORT)
            .withPushId(randomString())
            .build();
        mStrategy.doAction(mContext, wrapToIntent(notificationActionInfo));
        verify(mPushMessageTracker, times(1))
            .onNotificationInlineAdditionalAction(
                eq(notificationActionInfo.pushId),
                nullable(String.class),
                nullable(String.class),
                anyString(),
                eq(TRANSPORT),
                nullable(String.class)
            );
    }

    @Test
    public void doActionShouldNotReportInlineAdditionalActionToPushMessageTrackerIfPushIdIsNull() {
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
                anyString(),
                anyString()
            );
    }

    @Test
    public void doActionShouldNotReportInlineAdditionalActionToPushMessageTrackerIfPushIdIsEmpty() {
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
                anyString(),
                anyString()
            );
    }

    @Test
    public void doActionShouldReportPushIdToTracker() {
        String textReply = randomString();
        when(mRemoteInputBundle.getCharSequence(eq(KEY_TEXT_REPLY), anyString())).thenReturn(textReply);
        NotificationActionInfo notificationActionInfo = NotificationActionInfo.newBuilder(TRANSPORT)
            .withPushId(randomString())
            .withActionId(randomString())
            .withPayload(randomString())
            .build();
        mStrategy.doAction(mContext, wrapToIntent(notificationActionInfo));
        verify(mPushMessageTracker, times(1))
            .onNotificationInlineAdditionalAction(
                notificationActionInfo.pushId,
                notificationActionInfo.actionId,
                notificationActionInfo.payload,
                textReply,
                notificationActionInfo.transport,
                notificationActionInfo.targetActionUri
            );
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void doActionShouldClearNotificationIfApiNotArchived() {
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
    public void doActionShouldClearNotificationIfApiArchived() {
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
    public void setPushActiveToFalse() {
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
