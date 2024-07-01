package io.appmetrica.analytics.push.provider.firebase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import io.appmetrica.analytics.push.provider.firebase.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.firebase.impl.ControllerListUtils;
import io.appmetrica.analytics.push.provider.firebase.impl.CustomPushServiceController;
import io.appmetrica.analytics.push.provider.firebase.impl.DefaultPushServiceController;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class FirebasePushServiceControllerProviderTest {

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

        FirebasePushServiceControllerProvider pushServiceControllerProvider =
            new FirebasePushServiceControllerProvider(mContext);
        List<BasePushServiceController> controllerList =
            ReflectionHelpers.getField(pushServiceControllerProvider, "basePushServiceControllers");

        assertThat(controllerList.size()).isEqualTo(3);
        assertThat(controllerList.get(0) instanceof CustomPushServiceController).isEqualTo(true);
        assertThat(controllerList.get(1) instanceof DefaultPushServiceController).isEqualTo(true);
        assertThat(controllerList.get(2) instanceof BasePushServiceController).isEqualTo(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPushServiceControllerIfIdentifierEmpty() {
        ArrayList<BasePushServiceController> controllerList =
            ControllerListUtils.createControllerList(mContext, null, null);
        FirebasePushServiceControllerProvider pushServiceControllerProvider =
            new FirebasePushServiceControllerProvider(controllerList);

        pushServiceControllerProvider.getPushServiceController();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPushServiceControllerIfAppIdNotValid() {
        ArrayList<BasePushServiceController> controllerList =
            ControllerListUtils.createControllerList(mContext, null, new RandomStringGenerator().nextString());
        FirebasePushServiceControllerProvider pushServiceControllerProvider =
            new FirebasePushServiceControllerProvider(controllerList);

        pushServiceControllerProvider.getPushServiceController();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPushServiceControllerIfSenderIdNotValid() {
        ArrayList<BasePushServiceController> controllerList =
            ControllerListUtils.createControllerList(mContext, new RandomStringGenerator().nextString(), null);
        FirebasePushServiceControllerProvider pushServiceControllerProvider =
            new FirebasePushServiceControllerProvider(controllerList);

        pushServiceControllerProvider.getPushServiceController();
    }

    @Test
    public void testGetPushServiceControllerIfIdentifierValid() {
        ArrayList<BasePushServiceController> controllerList =
            ControllerListUtils.createControllerList(
                mContext,
                new RandomStringGenerator().nextString(),
                new RandomStringGenerator().nextString()
            );
        FirebasePushServiceControllerProvider pushServiceControllerProvider =
            new FirebasePushServiceControllerProvider(controllerList);

        assertThat(pushServiceControllerProvider.getPushServiceController()).isEqualTo(controllerList.get(0));
    }

}
