package io.appmetrica.analytics.push.impl.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainProcessDetectorTest {

    private MainProcessDetector mMainProcessDetector;

    @Before
    public void setUp() {
        mMainProcessDetector = new MainProcessDetector();
    }

    @Config(minSdk = ICE_CREAM_SANDWICH)
    @Test
    public void testIsMainProcess() {
        assertThat(mMainProcessDetector.isMainProcess()).isTrue();
    }
}
