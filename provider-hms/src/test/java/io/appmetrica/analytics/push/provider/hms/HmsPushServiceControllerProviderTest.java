package io.appmetrica.analytics.push.provider.hms;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import io.appmetrica.analytics.push.provider.hms.HmsPushServiceControllerProvider;
import io.appmetrica.analytics.push.provider.hms.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.hms.impl.ControllerListUtils;
import io.appmetrica.analytics.push.provider.hms.impl.DummyPushServiceController;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class HmsPushServiceControllerProviderTest {

    private Context mContext;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application);
    }

    @Test
    public void testInitControllerList() throws PackageManager.NameNotFoundException {
        ApplicationInfo info = new ApplicationInfo();
        PackageManager pm = mock(PackageManager.class);
        doReturn(info).when(pm).getApplicationInfo(anyString(), anyInt());
        doReturn(pm).when(mContext).getPackageManager();

        HmsPushServiceControllerProvider pushServiceControllerProvider = new HmsPushServiceControllerProvider(mContext);
        List<BasePushServiceController> controllerList =
            ReflectionHelpers.getField(pushServiceControllerProvider, "basePushServiceControllers");

        assertThat(controllerList.size()).isEqualTo(1);
        assertThat(controllerList.get(0) instanceof BasePushServiceController).isEqualTo(true);
    }

    @Test
    public void testGetPushServiceControllerIfAppIdNotValid() {
        ArrayList<BasePushServiceController> controllerList =
            ControllerListUtils.createControllerList(mContext, null);
        HmsPushServiceControllerProvider pushServiceControllerProvider =
            new HmsPushServiceControllerProvider(controllerList);

        assertThat(pushServiceControllerProvider.getPushServiceController())
            .isInstanceOf(DummyPushServiceController.class);
    }

}
