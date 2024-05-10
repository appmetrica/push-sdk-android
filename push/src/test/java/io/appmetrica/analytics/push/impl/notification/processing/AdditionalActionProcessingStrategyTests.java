package io.appmetrica.analytics.push.impl.notification.processing;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.notification.OpenActivityStrategy;
import io.appmetrica.analytics.push.impl.notification.OpenActivityStrategyTests;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AdditionalActionProcessingStrategyTests extends OpenActivityStrategyTests {

    private NotificationManager mNotificationManager;
    private AdditionalActionProcessingStrategy mStrategy;
    private AutoTrackingConfiguration mAutoTrackingConfiguration;
    private PushMessageHistory mPushMessageHistory;

    @Before
    public void setUp() {
        super.setUp();
        mNotificationManager = mock(NotificationManager.class);
        mStrategy = new AdditionalActionProcessingStrategy();
        mNotificationActionInfoBuilder.withTargetActionUri(randomString());
        mAutoTrackingConfiguration = mPushServiceProvider.getAutoTrackingConfiguration();
        when(mAutoTrackingConfiguration.isTrackingAdditionalAction(nullable(String.class))).thenReturn(true);
        mPushMessageHistory = mock(PushMessageHistory.class);
        mPushServiceProvider.setPushMessageHistory(mPushMessageHistory);
    }

    @Test
    public void testDoActionShouldReportOnAdditionalActionToTrackerIfPushIdIsNotEmpty() {
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withPushId(randomString()).build()));
        verify(mPushMessageTracker, times(1))
            .onNotificationAdditionalAction(anyString(), nullable(String.class), nullable(String.class), anyString());
    }

    @Test
    public void testDoActionShouldNotReportOnAdditionalActionToTrackerIfPushIdIsNull() {
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withPushId(null).build()));
        verify(mPushMessageTracker, never())
            .onNotificationAdditionalAction(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testDoActionShouldNotReportOnAdditionalActionToTrackerIfPushIdIsEmpty() {
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withPushId("").build()));
        verify(mPushMessageTracker, never())
            .onNotificationAdditionalAction(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testDoActionShouldReportPushIdToTracker() {
        String pushId = randomString();
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withPushId(pushId).build()));
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onNotificationAdditionalAction(arg.capture(), nullable(String.class), nullable(String.class), anyString());
        assertThat(arg.getValue()).isEqualTo(pushId);
    }

    @Test
    public void testDoActionShouldReportActionIdToTracker() {
        String actionId = randomString();
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withPushId(randomString()).withActionId(actionId).build())
        );
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mPushMessageTracker, times(1))
            .onNotificationAdditionalAction(anyString(), arg.capture(), nullable(String.class), anyString());
        assertThat(arg.getValue()).isEqualTo(actionId);
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.R)
    public void testDoActionShouldHideQuickControlPanelIfHideQuickControlPanelIsTrue() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withHideQuickControlPanel(true).build())
        );
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).sendBroadcast(arg.capture());
        assertThat(arg.getValue().getAction()).isEqualTo(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    @Test
    public void testDoActionShouldNotHideQuickControlPanelIfHideQuickControlPanelIsTrue() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withHideQuickControlPanel(true).build())
        );
        verify(mContext, never()).sendBroadcast(ArgumentMatchers.<Intent>any());
    }

    @Test
    public void testDoActionShouldDoNothingIfHideQuickControlPanelIsFalse() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withHideQuickControlPanel(false).build())
        );
        verify(mContext, never()).sendBroadcast(any(Intent.class));
    }

    @Test
    public void testDoActionShouldClearNotificationIfDismissOnAdditionalActionIsTrue() {
        String notificationTag = randomString();
        int notificationId = new Random().nextInt();
        NotificationActionInfo actionInfo = mNotificationActionInfoBuilder
            .withDismissOnAdditionalAction(true)
            .withNotificationTag(notificationTag)
            .withNotificationId(notificationId)
            .withPushId(randomString())
            .withActionId(randomString())
            .build();
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        mStrategy.doAction(mContext, wrapToIntent(actionInfo));
        verify(mNotificationManager).cancel(eq(notificationTag), eq(notificationId));
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(trackersHub, times(1)).reportEvent(
            eq("Clear notification by button"),
            arg.capture()
        );
        assertThat(arg.getValue()).contains(
            new AbstractMap.SimpleEntry<String, Object>("actionId", actionInfo.actionId),
            new AbstractMap.SimpleEntry<String, Object>("notificationId", actionInfo.notificationId),
            new AbstractMap.SimpleEntry<String, Object>("notificationTag", actionInfo.notificationTag),
            new AbstractMap.SimpleEntry<String, Object>("pushId", actionInfo.pushId)
        );
    }

    @Test
    public void testDoActionShouldDoNothingIfDismissOnAdditionalActionIsFalse() {
        mStrategy.doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withDismissOnAdditionalAction(false).build())
        );
        verify(mNotificationManager, never()).cancel(anyString(), anyInt());
    }

    @Test
    public void testDoActionShoulfNotReportOnNotificationAdditionalActionIsAutoTrackingAdditionalActionIsFalse() {
        when(mAutoTrackingConfiguration.isTrackingAdditionalAction(anyString())).thenReturn(false);
        mStrategy.doAction(
            mContext,
            wrapToIntent(
                NotificationActionInfo.newBuilder("")
                    .withPushId(randomString())
                    .withActionId(randomString())
                    .build()
            )
        );
        verify(mPushMessageTracker, never())
            .onNotificationAdditionalAction(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testDoNotStartActivityIfDoNothingIsTrue() {
        mStrategy.doAction(mContext, wrapToIntent(mNotificationActionInfoBuilder.withDoNothing(true).build()));
        verify(mContext, never()).startActivity(any(Intent.class));
    }

    @Test
    public void testSetPushActiveToFalseIfNotificationWasCanceled() {
        String pushId = randomString();
        NotificationActionInfo actionInfo = mNotificationActionInfoBuilder
            .withDismissOnAdditionalAction(true)
            .withPushId(pushId)
            .build();
        mStrategy.doAction(mContext, wrapToIntent(actionInfo));
        verify(mPushMessageHistory).setPushActive(eq(pushId), eq(false));
    }

    @Test
    public void testDoActionShouldReportToTrackersHubIfNoActionInfo() {
        mStrategy.doAction(mContext, new Intent());
        verify(trackersHub, times(1))
            .reportEvent("No action info for AdditionalActionProcessingStrategy");
    }

    @Test
    public void testDoActionShouldReportToTrackersHubIfNoNotificationManager() {
        NotificationActionInfo info = mNotificationActionInfoBuilder
            .withDismissOnAdditionalAction(true)
            .withPushId(randomString())
            .withActionId(randomString())
            .build();
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(null);
        mStrategy.doAction(mContext, wrapToIntent(info));
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(trackersHub, times(1)).reportEvent(
            eq("No notificationManager to clear notification by button"),
            arg.capture()
        );
        assertThat(arg.getValue()).contains(
            new AbstractMap.SimpleEntry<String, Object>("actionId", info.actionId),
            new AbstractMap.SimpleEntry<String, Object>("pushId", info.pushId)
        );
    }

    @Override
    protected OpenActivityStrategy getStrategy() {
        return mStrategy;
    }

    private Intent wrapToIntent(final NotificationActionInfo info) {
        return new Intent().putExtra(AppMetricaPush.EXTRA_ACTION_INFO, info);
    }
}
