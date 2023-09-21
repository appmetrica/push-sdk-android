package io.appmetrica.analytics.push.impl.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushIdFinderTests {

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private Context context;
    private PushIdFinder pushIdFinder;
    private AppMetricaPushCore appMetricaPushCore;
    private PushServiceProvider pushServiceProvider;
    private PushMessageHistory pushMessageHistory;
    private NotificationManager notificationManager;
    private TrackersHub trackersHub;

    @Before
    public void setUp() {
        context = mock(Context.class);
        trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        pushIdFinder = new PushIdFinder(context);
        appMetricaPushCore = AppMetricaPushCore.getInstance(context);
        pushServiceProvider = new MockablePushServiceProvider();
        appMetricaPushCore.setPushServiceProvider(pushServiceProvider);
        pushMessageHistory = pushServiceProvider.getPushMessageHistory();
        notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void testCallGetPushInfoByNotificationTagAndNotificationId() {
        String tag = randomString();
        int id = randomInt();
        pushIdFinder.findActive(tag, id);
        verify(pushMessageHistory).getPushInfoByNotificationTagAndNotificationId(eq(tag), eq(id));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void testNotFoundPushInfo() {
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(null);
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isNull();
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void testPushInfoNotContainsIsActive() {
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(randomString(), null)
        );
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isNull();
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void testPushInfoIsNotActive() {
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(randomString(), false)
        );
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isNull();
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void testPushInfoIsActive() {
        String pushId = randomString();
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(pushId, true)
        );
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isEqualTo(pushId);
    }

    @Test
    public void testPushInfoIsActiveAndNotificationInStatusBar() {
        String pushId = randomString();
        String tag = randomString();
        int id = randomInt();
        mockStatusBarNotification(tag, id);
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(pushId, true)
        );
        assertThat(pushIdFinder.findActive(tag, id)).isEqualTo(pushId);
        verify(trackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testPushInfoIsNotActiveAndNotificationInStatusBar() {
        String pushId = randomString();
        String tag = randomString();
        int id = randomInt();
        mockStatusBarNotification(tag, id);
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(pushId, false)
        );
        assertThat(pushIdFinder.findActive(tag, id)).isEqualTo(pushId);
        verify(trackersHub).reportError(anyString(), nullable(Throwable.class));
    }

    @Test
    public void testPushInfoNotFoundAndNotificationInStatusBar() {
        String pushId = randomString();
        String tag = randomString();
        int id = randomInt();
        mockStatusBarNotification(tag, id);
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(null);
        assertThat(pushIdFinder.findActive(tag, id)).isNull();
        verify(trackersHub).reportError(anyString(), nullable(Throwable.class));
    }

    @Test
    public void testPushInfoIsActiveAndNotificationNotInStatusBar() {
        String pushId = randomString();
        mockStatusBarNotification(randomString(), randomInt());
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(pushId, true)
        );
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isNull();
        verify(trackersHub).reportError(anyString(), nullable(Throwable.class));
    }

    @Test
    public void testPushInfoIsNotActiveAndNotificationNotInStatusBar() {
        String pushId = randomString();
        mockStatusBarNotification(randomString(), randomInt());
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(
            createPushInfo(pushId, false)
        );
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isNull();
        verify(trackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testPushInfoNotFoundAndNotificationNotInStatusBar() {
        mockStatusBarNotification(randomString(), randomInt());
        when(pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(anyString(), anyInt())).thenReturn(null);
        assertThat(pushIdFinder.findActive(randomString(), randomInt())).isNull();
        verify(trackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @NonNull
    private PushMessageHistory.PushInfo createPushInfo(@NonNull String pushId, @Nullable Boolean isActive) {
        return new PushMessageHistory.PushInfo(pushId, randomInt(), randomString(), isActive);
    }

    private void mockStatusBarNotification(@Nullable String tag, int id) {
        mockStatusBarNotification(new Pair[]{Pair.create(tag, id)});
    }

    private void mockStatusBarNotification(@NonNull Pair<String, Integer>[] ids) {
        StatusBarNotification[] notifications = new StatusBarNotification[ids.length];
        for (int i = 0; i < ids.length; i++) {
            StatusBarNotification notification = mock(StatusBarNotification.class);
            when(notification.getTag()).thenReturn(ids[i].first);
            when(notification.getId()).thenReturn(ids[i].second);
            notifications[i] = notification;
        }
        when(notificationManager.getActiveNotifications()).thenReturn(notifications);
    }
}
