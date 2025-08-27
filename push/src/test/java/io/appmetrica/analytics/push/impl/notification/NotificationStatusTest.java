package io.appmetrica.analytics.push.impl.notification;

import io.appmetrica.analytics.push.testutils.Rand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class NotificationStatusTest {

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
}
