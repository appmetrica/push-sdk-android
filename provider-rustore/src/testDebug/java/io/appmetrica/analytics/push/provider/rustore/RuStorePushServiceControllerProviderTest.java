package io.appmetrica.analytics.push.provider.rustore;

import android.app.Application;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.rustore.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.rustore.impl.Identifier;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ReflectionHelpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class RuStorePushServiceControllerProviderTest {

    @Mock
    private Application application;
    @Mock
    private BasePushServiceController controller;
    @Mock
    private Identifier identifier;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(controller.getIdentifier()).thenReturn(identifier);
    }

    @Test
    public void initControllerListCheck() {
        final RuStorePushServiceControllerProvider provider = new RuStorePushServiceControllerProvider(application);
        final List<BasePushServiceController> controllerList =
            ReflectionHelpers.getField(provider, "basePushServiceControllers");

        assertThat(controllerList.size()).isEqualTo(1);
        assertThat(controllerList.get(0) instanceof BasePushServiceController).isEqualTo(true);
    }

    public void testGetPushServiceController() {
        when(identifier.isEmpty()).thenReturn(false);
        when(identifier.isValid()).thenReturn(true);

        final RuStorePushServiceControllerProvider provider =
            new RuStorePushServiceControllerProvider(
                Arrays.asList(controller)
            );

        final PushServiceController pushServiceController = provider.getPushServiceController();
        assertThat(pushServiceController).isSameAs(controller);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPushServiceControllerIfIdentifierIsEmpty() {
        when(identifier.isEmpty()).thenReturn(true);
        when(identifier.isValid()).thenReturn(true);

        final RuStorePushServiceControllerProvider provider =
            new RuStorePushServiceControllerProvider(
                Arrays.asList(controller)
            );

        provider.getPushServiceController();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPushServiceControllerIfIdentifierIsNotValid() {
        when(identifier.isEmpty()).thenReturn(false);
        when(identifier.isValid()).thenReturn(false);

        final RuStorePushServiceControllerProvider provider =
            new RuStorePushServiceControllerProvider(
                Arrays.asList(controller)
            );

        provider.getPushServiceController();
    }
}
