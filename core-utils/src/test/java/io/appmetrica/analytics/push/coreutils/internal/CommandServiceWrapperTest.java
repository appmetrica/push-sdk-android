package io.appmetrica.analytics.push.coreutils.internal;

import android.content.Context;
import android.os.Bundle;

import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceCommandLauncher;
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceControllerProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class CommandServiceWrapperTest {

    private static final String TEST_PACKAGE = "com.test.package";

    @Mock
    private Context context;
    @Mock
    private Bundle bundle;
    @Mock
    private PushServiceControllerProvider provider;
    @Mock
    private PushServiceCommandLauncher serviceLauncher;
    @Mock
    private PushServiceCommandLauncher noServiceLauncher;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doReturn(TEST_PACKAGE).when(context).getPackageName();
        when(provider.getPushServiceCommandLauncher(true)).thenReturn(serviceLauncher);
        when(provider.getPushServiceCommandLauncher(false)).thenReturn(noServiceLauncher);
    }

    @Test
    public void startJobWithRightComponent() {
        final PushServiceFacade.CommandServiceWrapper wrapper = new PushServiceFacade.CommandServiceWrapper();
        wrapper.setPushServiceControllerProvider(provider);
        wrapper.startCommand(context, bundle);

        verify(serviceLauncher).launchService(same(bundle));
    }

    @Test
    public void startJobWithRightComponentIfDoesNotNeedService() {
        final PushServiceFacade.CommandServiceWrapper wrapper = new PushServiceFacade.CommandServiceWrapper();
        wrapper.setPushServiceControllerProvider(provider);
        wrapper.startCommand(context, bundle, false);

        verify(noServiceLauncher).launchService(same(bundle));
    }
}
