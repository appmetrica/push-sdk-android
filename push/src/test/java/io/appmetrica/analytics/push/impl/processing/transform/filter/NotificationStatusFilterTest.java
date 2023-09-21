package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class NotificationStatusFilterTest extends PushFilterTest {

    private NotificationManager notificationManager;
    private NotificationManagerCompat notificationManagerCompat;

    @Before
    public void setUp() {
        notificationManager = mock(NotificationManager.class);
        notificationManagerCompat = mock(NotificationManagerCompat.class);
        super.setUp(new NotificationStatusFilter(notificationManager, notificationManagerCompat));
        when(notificationManagerCompat.areNotificationsEnabled()).thenReturn(true);
    }

    @Test
    public void testDisabledAllNotification() {
        when(notificationManagerCompat.areNotificationsEnabled()).thenReturn(false);
        assertSilence(createPushMessage(randomString()));
    }

    @Test
    @Config(maxSdk = Build.VERSION_CODES.N_MR1)
    public void testEnabledNotificationOnApiLessOrEquals25() {
        assertShow(createPushMessage(randomString()));
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testDisabledNotificationChannel() {
        NotificationChannel channel = mock(NotificationChannel.class);
        when(channel.getImportance()).thenReturn(NotificationManager.IMPORTANCE_NONE);
        when(notificationManager.getNotificationChannel(anyString())).thenReturn(channel);
        assertSilence(createPushMessage(randomString()));
    }

    @Test
    @Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1})
    public void testEnabledNotificationOnApiEquals26Or27() {
        NotificationChannel channel = mock(NotificationChannel.class);
        when(channel.getImportance()).thenReturn(NotificationManager.IMPORTANCE_DEFAULT);
        when(notificationManager.getNotificationChannel(anyString())).thenReturn(channel);
        assertShow(createPushMessage(randomString()));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void testEnabledNotificationIfChannelNotExists() {
        when(notificationManager.getNotificationChannel(anyString())).thenReturn(null);
        assertShow(createPushMessage(randomString()));
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    public void testDisabledNotificationChannelGroup() {
        NotificationChannel channel = mock(NotificationChannel.class);
        when(channel.getImportance()).thenReturn(NotificationManager.IMPORTANCE_DEFAULT);
        when(channel.getGroup()).thenReturn(randomString());
        when(notificationManager.getNotificationChannel(anyString())).thenReturn(channel);

        NotificationChannelGroup group = mock(NotificationChannelGroup.class);
        when(group.isBlocked()).thenReturn(true);
        when(notificationManager.getNotificationChannelGroup(anyString())).thenReturn(group);

        assertSilence(createPushMessage(randomString()));
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    public void testEnabledNotificationOnApiMoreOrEquals28() {
        NotificationChannel channel = mock(NotificationChannel.class);
        when(channel.getImportance()).thenReturn(NotificationManager.IMPORTANCE_DEFAULT);
        when(channel.getGroup()).thenReturn(randomString());
        when(notificationManager.getNotificationChannel(anyString())).thenReturn(channel);

        NotificationChannelGroup group = mock(NotificationChannelGroup.class);
        when(group.isBlocked()).thenReturn(false);
        when(notificationManager.getNotificationChannelGroup(anyString())).thenReturn(group);
        assertShow(createPushMessage(randomString()));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.P)
    public void testEnabledNotificationIfChannelGroupNotExists() {
        NotificationChannel channel = mock(NotificationChannel.class);
        when(channel.getImportance()).thenReturn(NotificationManager.IMPORTANCE_DEFAULT);
        when(channel.getGroup()).thenReturn(randomString());
        when(notificationManager.getNotificationChannel(anyString())).thenReturn(channel);

        when(notificationManager.getNotificationChannelGroup(anyString())).thenReturn(null);
        assertShow(createPushMessage(randomString()));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void testUseDefaultChannelIdIfDefinedNull() {
        pushFilter.filter(createPushMessage(null));
        verify(notificationManager, times(1))
            .getNotificationChannel(eq(NotificationChannelController.DEFAULT_CHANNEL_ID));
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.O)
    public void testUseDefaultChannelIdIfDefinedEmpty() {
        pushFilter.filter(createPushMessage(""));
        verify(notificationManager, times(1))
            .getNotificationChannel(eq(NotificationChannelController.DEFAULT_CHANNEL_ID));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable String channelId) {
        PushNotification pushNotification = mock(PushNotification.class);
        when(pushNotification.getChannelId()).thenReturn(channelId);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getNotification()).thenReturn(pushNotification);
        return pushMessage;
    }
}
