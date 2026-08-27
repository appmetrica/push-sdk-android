package io.appmetrica.analytics.push.coreutils.internal.service;

import android.os.Build;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.N_MR1)
public class PushServiceControllerProviderPreOTests {

    private PushServiceControllerProvider mControllerProvider;

    @Before
    public void setUp() {
        mControllerProvider = new PushServiceControllerProvider(RuntimeEnvironment.application);
    }

    @Test
    public void testGetConfigurationServiceCommandLauncher() {
        assertThat(mControllerProvider.getPushServiceCommandLauncher())
            .isInstanceOf(PushServiceController.class);
    }
}
