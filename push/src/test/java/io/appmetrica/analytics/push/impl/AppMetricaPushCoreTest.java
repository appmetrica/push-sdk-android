package io.appmetrica.analytics.push.impl;

import io.appmetrica.analytics.push.TokenUpdateListener;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaPushCoreTest {

    private final HashMap<String, String> tokens = new HashMap<String, String>();
    private AppMetricaPushCore core = new AppMetricaPushCore(RuntimeEnvironment.application);

    @Test
    public void updateTokens() {
        tokens.put(CoreConstants.Transport.FIREBASE, CoreConstants.Transport.FIREBASE);
        tokens.put(CoreConstants.Transport.GCM, CoreConstants.Transport.GCM);
        tokens.put(CoreConstants.Transport.HMS, CoreConstants.Transport.HMS);
        core.updateTokens(tokens);
        assertThat(core.getTokens()).isEqualTo(tokens);
    }

    @Test
    public void callTokenUpdateListener() {
        TokenUpdateListener listener = mock(TokenUpdateListener.class);
        core.setTokenUpdateListener(listener);
        tokens.put(CoreConstants.Transport.FIREBASE, CoreConstants.Transport.FIREBASE);
        tokens.put(CoreConstants.Transport.GCM, CoreConstants.Transport.GCM);
        tokens.put(CoreConstants.Transport.HMS, CoreConstants.Transport.HMS);
        core.updateTokens(tokens);
        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(listener).onTokenUpdated(captor.capture());
        assertThat(captor.getValue()).isEqualTo(tokens);
    }
}
