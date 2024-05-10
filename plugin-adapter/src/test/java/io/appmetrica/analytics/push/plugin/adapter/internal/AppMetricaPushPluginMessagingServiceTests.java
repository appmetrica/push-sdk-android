package io.appmetrica.analytics.push.plugin.adapter.internal;

import android.content.Context;
import com.google.firebase.messaging.RemoteMessage;
import io.appmetrica.analytics.push.plugin.adapter.impl.Initializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaPushPluginMessagingServiceTests {

    private AppMetricaPushPluginMessagingService appMetricaPushPluginMessagingService;

    private final Initializer initializer = mock(Initializer.class);

    @Before
    public void setUp() {
        appMetricaPushPluginMessagingService =
            spy(Robolectric.buildService(AppMetricaPushPluginMessagingService.class).get());
        doReturn(initializer).when(appMetricaPushPluginMessagingService).getInitializer();
    }

    @Test
    public void superOnMessageCalled() {
        RemoteMessage message = mock(RemoteMessage.class);
        appMetricaPushPluginMessagingService.onMessageReceived(message);

        verify(appMetricaPushPluginMessagingService).processPush(any(Context.class), same(message));
    }

    @Test
    public void superOnTokenRefreshed() {
        String newToken = "newToken";
        appMetricaPushPluginMessagingService.onNewToken(newToken);

        verify(appMetricaPushPluginMessagingService).processToken(any(Context.class), eq(newToken));
    }

    @Test
    public void testOnCreateShouldInitMetricaAndMetricaPush() {
        appMetricaPushPluginMessagingService.onCreate();

        verify(initializer).initIfNeeded();
    }
}
