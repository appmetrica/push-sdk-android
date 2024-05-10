package io.appmetrica.analytics.push.impl.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class NotificationChannelControllerTests {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationChannelController mNotificationChannelController;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mNotificationManager = mock(NotificationManager.class);
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        mNotificationChannelController = new NotificationChannelController(mContext);
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.N_MR1)
    public void testNotCreateDefaultChannelIfApiLessO() {
        mNotificationChannelController.createDefaultChannel();
        verifyNoInteractions(mNotificationManager);
        assertThat(mNotificationChannelController.getDefaultChannel()).isNull();
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testCreateDefaultChannel() {
        mNotificationChannelController.createDefaultChannel();
        ArgumentCaptor<NotificationChannel> arg = ArgumentCaptor.forClass(NotificationChannel.class);
        verify(mNotificationManager).createNotificationChannel(arg.capture());
        assertThat(arg.getValue().getId()).isEqualTo(NotificationChannelController.DEFAULT_CHANNEL_ID);
        assertThat(arg.getValue().getName()).isEqualTo(NotificationChannelController.DEFAULT_CHANNEL_NAME);
        assertThat(arg.getValue().getImportance()).isEqualTo(NotificationManager.IMPORTANCE_LOW);
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testCreateDefaultChannelWithOtherName() {
        String name = new RandomStringGenerator().nextString();
        mNotificationChannelController.getDefaultChannel().setName(name);
        mNotificationChannelController.createDefaultChannel();
        ArgumentCaptor<NotificationChannel> arg = ArgumentCaptor.forClass(NotificationChannel.class);
        verify(mNotificationManager).createNotificationChannel(arg.capture());
        assertThat(arg.getValue().getName()).isEqualTo(name);
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testCreateDefaultChannelWithOtherImportance() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        mNotificationChannelController.getDefaultChannel().setImportance(importance);
        mNotificationChannelController.createDefaultChannel();
        ArgumentCaptor<NotificationChannel> arg = ArgumentCaptor.forClass(NotificationChannel.class);
        verify(mNotificationManager).createNotificationChannel(arg.capture());
        assertThat(arg.getValue().getImportance()).isEqualTo(importance);
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testRestoreDefaultChannel() {
        String name = new RandomStringGenerator().nextString();
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel =
            new NotificationChannel(NotificationChannelController.DEFAULT_CHANNEL_ID, name, importance);
        when(mNotificationManager.getNotificationChannel(NotificationChannelController.DEFAULT_CHANNEL_ID))
            .thenReturn(channel);

        NotificationChannelController controller = new NotificationChannelController(mContext);
        assertThat(controller.getDefaultChannel().getId()).isEqualTo(NotificationChannelController.DEFAULT_CHANNEL_ID);
        assertThat(controller.getDefaultChannel().getName()).isEqualTo(name);
        assertThat(controller.getDefaultChannel().getImportance()).isEqualTo(importance);
    }
}
