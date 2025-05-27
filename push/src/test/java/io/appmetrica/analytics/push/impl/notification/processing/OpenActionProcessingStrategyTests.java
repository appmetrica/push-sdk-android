package io.appmetrica.analytics.push.impl.notification.processing;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.notification.OpenActivityStrategy;
import io.appmetrica.analytics.push.impl.notification.OpenActivityStrategyTests;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class OpenActionProcessingStrategyTests extends OpenActivityStrategyTests {

    private OpenActionProcessingStrategy mStrategy;
    private PackageManager mPackageManager;
    private PushMessageHistory mPushMessageHistory;

    @Before
    public void setUp() {
        super.setUp();
        mStrategy = new OpenActionProcessingStrategy();
        mPackageManager = mock(PackageManager.class);
        when(mContext.getPackageManager()).thenReturn(mPackageManager);
        mPushServiceProvider.setAutoTrackingConfiguration(AutoTrackingConfiguration.newBuilder().build());
        mPushMessageHistory = mock(PushMessageHistory.class);
        mPushServiceProvider.setPushMessageHistory(mPushMessageHistory);
    }

    @Test
    public void doActionShouldReportOnOpenNotificationToTrackerIfPushIdIsNotEmpty() {
        NotificationActionInfo notificationActionInfo = mNotificationActionInfoBuilder
            .withPushId(randomString())
            .build();
        mStrategy.doAction(mContext, wrapToIntent(notificationActionInfo));
        verify(mPushMessageTracker).onPushOpened(
            eq(notificationActionInfo.pushId),
            nullable(String.class),
            anyString(),
            nullable(String.class)
        );
    }

    @Test
    public void doActionShouldNotReportOnOpenNotificationToTrackerIfPushIdIsNull() {
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withPushId(null).build()));
        verify(mPushMessageTracker, never()).onPushOpened(
            anyString(),
            anyString(),
            anyString(),
            anyString()
        );
    }

    @Test
    public void doActionShouldNotReportOnOpenNotificationToTrackerIfPushIdIsEmpty() {
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withPushId("").build()));
        verify(mPushMessageTracker, never()).onPushOpened(
            anyString(),
            anyString(),
            anyString(),
            anyString()
        );
    }

    @Test
    public void doActionShouldNotStartActivityIfActionNotDefinedAndDefaultIntentIsNotExists() {
        when(mPackageManager.getLaunchIntentForPackage(anyString())).thenReturn(null);
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withTargetActionUri(null).withActionId(null).build())
        );
        verify(mContext, never()).startActivity(any(Intent.class));
    }

    @Test
    public void doActionShouldStartActivityIfActionNotDefinedAndDefaultIntentExistsByActivityName() {
        Activity activity = new Activity();
        ComponentName componentName = mock(ComponentName.class);
        when(componentName.getClassName()).thenReturn(activity.getClass().getName());
        Intent intent = new Intent();
        intent.setComponent(componentName);
        when(mPackageManager.getLaunchIntentForPackage(anyString())).thenReturn(intent);
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withTargetActionUri(null).withActionId(null).build())
        );
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext).startActivity(arg.capture());
        assertThat(arg.getValue().getComponent().getClassName()).isEqualTo(activity.getClass().getName());
        assertThat(arg.getValue().getAction()).isEqualTo("io.appmetrica.analytics.push.action.OPEN");
    }

    @Test
    public void doActionShouldStartActivityIfActionNotDefinedAndDefaultIntentExistsWithUnknownActivityName() {
        ComponentName componentName = mock(ComponentName.class);
        when(componentName.getClassName())
            .thenReturn("some.undefined.Activity"); // e.g. activity-alias name left for backport
        Intent intent = new Intent();
        intent.setComponent(componentName);
        when(mPackageManager.getLaunchIntentForPackage(anyString())).thenReturn(intent);
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withTargetActionUri(null).withActionId(null).build())
        );
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext).startActivity(arg.capture());
        assertThat(arg.getValue()).isEqualTo(intent);
        assertThat(arg.getValue().getAction()).isEqualTo("io.appmetrica.analytics.push.action.OPEN");
    }

    @Test
    public void doActionShouldAddActionPayloadIfActionNotDefinedAndDefaultActionExists() {
        String payload = randomString();
        Activity activity = new Activity();
        ComponentName componentName = mock(ComponentName.class);
        when(componentName.getClassName()).thenReturn(activity.getClass().getName());
        Intent intent = new Intent();
        intent.setComponent(componentName);
        when(mPackageManager.getLaunchIntentForPackage(anyString())).thenReturn(intent);
        mStrategy.doAction(
            mContext,
            wrapToIntent(
                mNotificationActionInfoBuilder
                    .withPayload(payload)
                    .withTargetActionUri(null)
                    .withActionId(null)
                    .build()
            )
        );
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext).startActivity(arg.capture());
        assertThat(arg.getValue().getStringExtra(AppMetricaPush.EXTRA_PAYLOAD)).isEqualTo(payload);
    }

    @Test
    public void doActionShouldReportPushIdToTracker() {
        NotificationActionInfo notificationActionInfo = mNotificationActionInfoBuilder
            .withPushId(randomString())
            .withActionId(randomString())
            .withPayload(randomString())
            .build();
        mStrategy.doAction(mContext, wrapToIntent(notificationActionInfo));
        verify(mPushMessageTracker).onPushOpened(
            notificationActionInfo.pushId,
            notificationActionInfo.payload,
            notificationActionInfo.transport,
            notificationActionInfo.targetActionUri
        );
    }

    @Test
    public void detPushActiveToFalse() {
        String pushId = randomString();
        NotificationActionInfo actionInfo = NotificationActionInfo.newBuilder("")
            .withPushId(pushId)
            .build();
        mStrategy.doAction(mContext, wrapToIntent(actionInfo));
        verify(mPushMessageHistory).setPushActive(eq(pushId), eq(false));
    }

    @Override
    protected OpenActivityStrategy getStrategy() {
        return mStrategy;
    }

    private Intent wrapToIntent(final NotificationActionInfo info) {
        return new Intent().putExtra(AppMetricaPush.EXTRA_ACTION_INFO, info);
    }
}
