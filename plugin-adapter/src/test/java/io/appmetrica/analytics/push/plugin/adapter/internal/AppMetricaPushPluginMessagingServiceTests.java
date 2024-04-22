package io.appmetrica.analytics.push.plugin.adapter.internal;

import android.content.Context;
import com.google.firebase.messaging.RemoteMessage;
import io.appmetrica.analytics.push.plugin.adapter.impl.Initializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
        doReturn(true).when(initializer).initIfNeeded();

        RemoteMessage message = mock(RemoteMessage.class);
        appMetricaPushPluginMessagingService.onMessageReceived(message);

        verify(appMetricaPushPluginMessagingService).processPush(any(Context.class), same(message));
    }

    @Test
    public void superOnMessageNotCalled() {
        doReturn(false).when(initializer).initIfNeeded();

        RemoteMessage message = mock(RemoteMessage.class);
        appMetricaPushPluginMessagingService.onMessageReceived(message);

        verify(appMetricaPushPluginMessagingService, never()).processPush(any(Context.class), any(RemoteMessage.class));
    }

    @Test
    public void superOnTokenRefreshed() {
        doReturn(true).when(initializer).initIfNeeded();

        String newToken = "newToken";
        appMetricaPushPluginMessagingService.onNewToken(newToken);

        verify(appMetricaPushPluginMessagingService).processToken(any(Context.class), eq(newToken));
    }

    @Test
    public void superOnTokenRefreshedNotCalled() {
        doReturn(false).when(initializer).initIfNeeded();

        appMetricaPushPluginMessagingService.onNewToken("newToken");

        verify(appMetricaPushPluginMessagingService, never()).processToken(any(Context.class), anyString());
    }

    @Test
    public void testOnCreateShouldInitMetricaAndMetricaPush() {
        appMetricaPushPluginMessagingService.onCreate();

        verify(initializer).initIfNeeded();
    }
}
