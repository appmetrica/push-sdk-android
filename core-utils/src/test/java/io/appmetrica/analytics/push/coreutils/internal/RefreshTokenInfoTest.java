package io.appmetrica.analytics.push.coreutils.internal;

import android.os.Bundle;
import io.appmetrica.analytics.push.testutils.Rand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RefreshTokenInfoTest {

    private static final String FORCE_KEY = "FORCE";
    private static final String NOTIFICATION_STATUS_CHANGED_TIME_KEY = "NOTIFICATION_STATUS_CHANGED_TIME_KEY";

    @Test
    public void testForce() {
        assertThat(new RefreshTokenInfo(true).force).isTrue();
        assertThat(new RefreshTokenInfo(false).force).isFalse();
    }

    @Test
    public void testNotificationStatusChangedTime() {
        assertThat(new RefreshTokenInfo(false).notificationStatusChangedTime).isNull();
        assertThat(new RefreshTokenInfo(false, null).notificationStatusChangedTime).isNull();
        long time = Rand.randomLong();
        assertThat(new RefreshTokenInfo(false, time).notificationStatusChangedTime).isEqualTo(time);
    }

    @Test
    public void testToBundle() {
        boolean force = Rand.randomBoolean();
        long time = Rand.randomLong();
        Bundle bundle = new RefreshTokenInfo(force, time).toBundle();
        assertThat(bundle.getBoolean(FORCE_KEY)).isEqualTo(force);
        assertThat(bundle.getLong(NOTIFICATION_STATUS_CHANGED_TIME_KEY)).isEqualTo(time);
    }

    @Test
    public void testFromBundle() {
        boolean force = Rand.randomBoolean();
        long time = Rand.randomLong();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FORCE_KEY, force);
        bundle.putLong(NOTIFICATION_STATUS_CHANGED_TIME_KEY, time);
        RefreshTokenInfo info = RefreshTokenInfo.fromBundle(bundle);
        assertThat(info.force).isEqualTo(force);
        assertThat(info.notificationStatusChangedTime).isEqualTo(time);
    }
}
