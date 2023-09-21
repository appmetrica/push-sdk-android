package io.appmetrica.analytics.push.coreutils.internal.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushServiceControllerTest {

    private static final String PACKAGE_NAME = "io.appmetrica.analytics.push.test.package_name";
    private static final String PUSH_SERVICE = "io.appmetrica.analytics.push.internal.service.PushService";

    private Context mContext;
    private final Bundle mCommandBundle = new Bundle();

    private PushServiceController mConfigurationServiceController;

    @Before
    public void setUp() throws Exception {
        mContext = mock(Context.class);
        when(mContext.getPackageName()).thenReturn(PACKAGE_NAME);

        mConfigurationServiceController = new PushServiceController(mContext);
    }

    @Test
    public void testLaunchCommandCreatesJobInfoWithValidId() {
        mConfigurationServiceController.launchService(mCommandBundle);
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).startService(arg.capture());
        final Intent intent = arg.getValue();
        assertThat(intent.getExtras()).isEqualToComparingFieldByField(mCommandBundle);
        assertThat(intent.getComponent().getClassName()).isEqualTo(PUSH_SERVICE);
    }
}
