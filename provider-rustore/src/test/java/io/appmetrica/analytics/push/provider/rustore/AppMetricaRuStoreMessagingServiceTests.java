package io.appmetrica.analytics.push.provider.rustore;

import android.content.Context;
import android.os.Bundle;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import ru.rustore.sdk.pushclient.messaging.model.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaRuStoreMessagingServiceTests {

    @Rule
    public final MockedStaticRule<TrackersHub> trackersHubMockedStaticRule =
        new MockedStaticRule<>(TrackersHub.class);
    @Rule
    public final MockedStaticRule<PushServiceFacade> pushServiceFacadeMockedStaticRule =
        new MockedStaticRule<>(PushServiceFacade.class);

    @Mock
    private TrackersHub trackersHub;
    @Mock
    private PushServiceFacade.CommandServiceWrapper commandServiceWrapper;

    private AppMetricaRuStoreMessagingService service;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper);

        service = new AppMetricaRuStoreMessagingService();
    }

    @Test
    public void onMessageReceivedWithDataPayload() {
        final String key = "yamp";
        final String value = "data";
        final Map<String, String> data = new HashMap<>();
        data.put(key, value);

        final RemoteMessage remoteMessage = mock(RemoteMessage.class);
        when(remoteMessage.getData()).thenReturn(data);

        final ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);

        service.onMessageReceived(remoteMessage);

        verify(trackersHub).reportEvent("RuStoreMessagingService receive push");
        pushServiceFacadeMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                PushServiceFacade.processPush(
                    any(Context.class),
                    dataArg.capture(),
                    eq(CoreConstants.Transport.RUSTORE)
                );
            }
        });
        assertThat(dataArg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void onMessageReceivedWithoutDataPayload() {
        final Map<String, String> data = new HashMap<>();
        final RemoteMessage remoteMessage = mock(RemoteMessage.class);
        when(remoteMessage.getData()).thenReturn(data);

        final ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);

        service.onMessageReceived(remoteMessage);

        verify(trackersHub).reportEvent("RuStoreMessagingService receive push");
        pushServiceFacadeMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                PushServiceFacade.processPush(
                    any(Context.class),
                    dataArg.capture(),
                    eq(CoreConstants.Transport.RUSTORE)
                );
            }
        });
        assertThat(dataArg.getValue().isEmpty()).isTrue();
    }

    @Test
    public void onTokenRefresh() {
        final String key = "yamp";
        final String value = "data";
        final Map<String, String> data = new HashMap<>();
        data.put(key, value);

        final RemoteMessage remoteMessage = mock(RemoteMessage.class);
        when(remoteMessage.getData()).thenReturn(data);

        final ArgumentCaptor<Bundle> dataArg = ArgumentCaptor.forClass(Bundle.class);

        service.onMessageReceived(remoteMessage);

        verify(trackersHub).reportEvent("RuStoreMessagingService receive push");
        pushServiceFacadeMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                PushServiceFacade.processPush(
                    any(Context.class),
                    dataArg.capture(),
                    eq(CoreConstants.Transport.RUSTORE)
                );
            }
        });
        assertThat(dataArg.getValue().getString(key)).isEqualTo(value);
    }

    @Test
    public void testOnTokenRefresh() {
        final String token = randomString();

        service.onNewToken(token);

        verify(trackersHub).reportEvent("RuStoreMessagingService refresh token");
        pushServiceFacadeMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                PushServiceFacade.refreshToken(any(Context.class));
            }
        });
    }
}
