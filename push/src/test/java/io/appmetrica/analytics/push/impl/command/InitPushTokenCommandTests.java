package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.RefreshTokenInfo;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.storage.Token;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class InitPushTokenCommandTests {

    private static final String REFRESH_TOKEN_INFO = "io.appmetrica.analytics.push.REFRESH_TOKEN_INFO";

    @Rule
    public final MockedStaticRule<AppMetricaPushCore> sAppMetricaPushCore =
        new MockedStaticRule<>(AppMetricaPushCore.class);

    private Context context;
    private PreferenceManager preferenceManager;
    private String pushToken;
    private final Map<String, String> tokens = new HashMap<String, String>();
    private AppMetricaPushCore appMetricaPushCore;

    @Before
    public void setUp() {
        context = mock(Context.class);
        pushToken = new RandomStringGenerator().nextString();
        tokens.put(CoreConstants.Transport.FIREBASE, pushToken);

        appMetricaPushCore = mock(AppMetricaPushCore.class);
        when(AppMetricaPushCore.getInstance(context)).thenReturn(appMetricaPushCore);
        when(appMetricaPushCore.isInitialized()).thenReturn(true);

        PushServiceControllerComposite pushServiceControllerComposite = mock(PushServiceControllerComposite.class);
        when(appMetricaPushCore.getPushServiceController()).thenReturn(pushServiceControllerComposite);
        when(pushServiceControllerComposite.getTokens()).thenReturn(tokens);

        PushServiceProvider pushServiceProvider = new MockablePushServiceProvider();
        when(appMetricaPushCore.getPushServiceProvider()).thenReturn(pushServiceProvider);

        preferenceManager = mock(PreferenceManager.class);
        when(appMetricaPushCore.getPreferenceManager()).thenReturn(preferenceManager);
    }

    @Test
    public void testInitPushTokenCommandShouldNotifyOnFirstTokenReceived() throws Exception {
        InitPushTokenCommand command = new InitPushTokenCommand();
        command.execute(context, new Bundle());
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> sharedArg = ArgumentCaptor.forClass(String.class);
        verify(appMetricaPushCore, times(1)).onFirstTokenReceived(arg.capture());
        verify(preferenceManager, times(1)).saveTokens(sharedArg.capture());
        assertThat(arg.getValue()).isEqualTo(tokens);
        assertThat(Token.parseTokens(sharedArg.getValue()).get(CoreConstants.Transport.FIREBASE).token)
            .isEqualTo(pushToken);
    }

    @Test
    public void testInitPushTokenCommandShouldNotifyOnFirstTokenReceivedIfReceivedOldTokenButForceIsSet() {
        mockTokens();

        InitPushTokenCommand command = new InitPushTokenCommand();
        final Bundle bundle = new Bundle();
        bundle.putBundle(REFRESH_TOKEN_INFO, new RefreshTokenInfo(true).toBundle());
        command.execute(context, bundle);
        verify(appMetricaPushCore, times(1)).onFirstTokenReceived(any(Map.class));
        verify(preferenceManager, times(1)).saveTokens(anyString());
    }

    @Test
    public void testInitPushTokenCommandShouldNotNotifyOnFirstTokenReceivedIfReceivedOldToken() {
        mockTokens();

        InitPushTokenCommand command = new InitPushTokenCommand();
        command.execute(context, new Bundle());
        verify(appMetricaPushCore, times(0)).onFirstTokenReceived(any(Map.class));
        verify(preferenceManager, times(0)).saveTokens(anyString());
    }

    @Test
    public void testInitPushTokenCommandShouldNotifyOnFirstTokenReceivedIfReceivedOldTokenButBeforeDayAgo() {
        InitPushTokenCommand command = new InitPushTokenCommand();
        command.execute(context, new Bundle());
        verify(appMetricaPushCore, times(1)).onFirstTokenReceived(any(Map.class));
        verify(preferenceManager, times(1)).saveTokens(anyString());
    }

    private void mockTokens() {
        when(preferenceManager.getTokens()).thenReturn(Token.saveToString(tokens, System.currentTimeMillis()));
    }
}
