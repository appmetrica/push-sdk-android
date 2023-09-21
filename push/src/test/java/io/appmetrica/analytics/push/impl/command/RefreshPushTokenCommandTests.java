package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.storage.Token;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class RefreshPushTokenCommandTests {

    @Rule
    public final MockedStaticRule<AppMetricaPushCore> sAppMetricaPushCore =
        new MockedStaticRule<>(AppMetricaPushCore.class);

    private String pushToken;
    private final Map<String, String> tokens = new HashMap<String, String>();

    @Before
    public void setUp() {
        Context context = mock(Context.class);
        pushToken = new RandomStringGenerator().nextString();
        tokens.put(CoreConstants.Transport.FIREBASE, pushToken);

        AppMetricaPushCore appMetricaPushCore = mock(AppMetricaPushCore.class);
        when(AppMetricaPushCore.getInstance(context)).thenReturn(appMetricaPushCore);
        when(appMetricaPushCore.isInitialized()).thenReturn(true);

        PushServiceControllerComposite pushServiceControllerComposite = mock(PushServiceControllerComposite.class);
        when(appMetricaPushCore.getPushServiceController()).thenReturn(pushServiceControllerComposite);
        when(pushServiceControllerComposite.getTokens()).thenReturn(tokens);

        PushServiceProvider pushServiceProvider = new MockablePushServiceProvider();
        when(appMetricaPushCore.getPushServiceProvider()).thenReturn(pushServiceProvider);

        PreferenceManager preferenceManager = mock(PreferenceManager.class);
        when(appMetricaPushCore.getPreferenceManager()).thenReturn(preferenceManager);
    }

    @Test
    public void notEqualTokensSize() {
        assertThat(
            RefreshPushTokenCommand.shouldUpdateTokens(tokens, Collections.<String, Token>emptyMap(), 1)
        ).isTrue();
    }

    @Test
    public void differentProviders() {
        assertThat(
            RefreshPushTokenCommand.shouldUpdateTokens(
                tokens,
                Collections.singletonMap(CoreConstants.Transport.GCM, new Token("a", 1)),
                1
            )
        ).isTrue();
    }

    @Test
    public void differentTokens() {
        assertThat(
            RefreshPushTokenCommand.shouldUpdateTokens(
                tokens,
                Collections.singletonMap(CoreConstants.Transport.FIREBASE, new Token("a", 1)),
                1
            )
        ).isTrue();
    }

    @Test
    public void expiredToken() {
        assertThat(
            RefreshPushTokenCommand.shouldUpdateTokens(
                tokens,
                Collections.singletonMap(CoreConstants.Transport.FIREBASE, new Token(pushToken, 1)),
                TimeUnit.DAYS.toMillis(2) + 1
            )
        ).isTrue();
    }
}
