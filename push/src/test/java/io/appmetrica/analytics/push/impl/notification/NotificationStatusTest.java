package io.appmetrica.analytics.push.impl.notification;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.testutils.Rand;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class NotificationStatusTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testGroupShouldNotChangeChannels() {
        NotificationStatus.Group group = new NotificationStatus.Group(
            randomString(),
            new HashSet<NotificationStatus.Channel>(),
            true,
            false
        );
        group.channels.add(new NotificationStatus.Channel(randomString(), true, false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGroupShouldNotChangeChannelsWithNullChannels() {
        NotificationStatus.Group group = new NotificationStatus.Group(
            randomString(),
            null,
            true,
            false
        );
        group.channels.add(new NotificationStatus.Channel(randomString(), true, false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotificationStatusNotChangeGroups() {
        NotificationStatus notificationStatus = new NotificationStatus(
            new HashSet<NotificationStatus.Group>(),
            new HashSet<NotificationStatus.Channel>(),
            true,
            false
        );
        NotificationStatus.Group group = new NotificationStatus.Group(
            randomString(),
            null,
            true,
            false
        );
        notificationStatus.groups.add(group);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotificationStatusNotChangeChannelsWithoutGroup() {
        NotificationStatus notificationStatus = new NotificationStatus(
            new HashSet<NotificationStatus.Group>(),
            new HashSet<NotificationStatus.Channel>(),
            true,
            false
        );
        notificationStatus.channelsWithoutGroup.add(new NotificationStatus.Channel(randomString(), true, false));
    }

    @Test
    public void testChangedTimeDefault() {
        NotificationStatus notificationStatus = new NotificationStatus(true, false);
        assertThat(notificationStatus.getChangedTime()).isNull();
    }

    @Test
    public void testChangedTime() {
        NotificationStatus notificationStatus = new NotificationStatus(true, false);
        long time = Rand.randomLong();
        notificationStatus.setChangedTime(time);
        assertThat(notificationStatus.getChangedTime()).isEqualTo(time);
    }

    @Test
    public void testChangedTimeReset() {
        NotificationStatus notificationStatus = new NotificationStatus(true, false);
        notificationStatus.setChangedTime(Rand.randomLong());
        notificationStatus.setChangedTime(null);
        assertThat(notificationStatus.getChangedTime()).isNull();
    }

    @NonNull
    private String randomString() {
        return new RandomStringGenerator().nextString();
    }
}
