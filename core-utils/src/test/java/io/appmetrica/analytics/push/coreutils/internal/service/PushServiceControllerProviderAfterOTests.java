package io.appmetrica.analytics.push.coreutils.internal.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PushServiceControllerProviderAfterOTests {

    private PushServiceControllerProvider mControllerProvider;

    @Before
    public void setUp() {
        mControllerProvider = new PushServiceControllerProvider(RuntimeEnvironment.application);
    }

    @Test
    public void testGetConfigurationServiceCommandLauncher() {
        assertThat(mControllerProvider.getPushServiceCommandLauncher())
            .isInstanceOf(PushJobServiceController.class);
    }
}
