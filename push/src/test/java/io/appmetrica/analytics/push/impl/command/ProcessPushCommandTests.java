package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.processing.PushProcessor;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ProcessPushCommandTests {

    @Rule
    public final MockedStaticRule<AppMetricaPushCore> sAppMetricaPushCore =
        new MockedStaticRule<>(AppMetricaPushCore.class);

    private Context context;
    private final Map<String, String> tokens = new HashMap<String, String>();
    private PushProcessor pushProcessor;

    @Before
    public void setUp() {
        context = mock(Context.class);
        tokens.put(CoreConstants.Transport.FIREBASE, new RandomStringGenerator().nextString());

        AppMetricaPushCore appMetricaPushCore = mock(AppMetricaPushCore.class);
        when(AppMetricaPushCore.getInstance(context)).thenReturn(appMetricaPushCore);
        when(appMetricaPushCore.isInitialized()).thenReturn(true);

        PushServiceControllerComposite pushServiceControllerComposite = mock(PushServiceControllerComposite.class);
        when(appMetricaPushCore.getPushServiceController()).thenReturn(pushServiceControllerComposite);
        when(pushServiceControllerComposite.getTokens()).thenReturn(tokens);

        PushServiceProvider pushServiceProvider = new MockablePushServiceProvider();
        when(appMetricaPushCore.getPushServiceProvider()).thenReturn(pushServiceProvider);

        pushProcessor = pushServiceProvider.getPushProcessor();

        PreferenceManager preferenceManager = mock(PreferenceManager.class);
        when(appMetricaPushCore.getPreferenceManager()).thenReturn(preferenceManager);
    }

    @Test
    public void testProcessPushCommandShouldSendPushToProcessor() {
        Bundle bundle = new Bundle();
        bundle.putString(new RandomStringGenerator().nextString(), new RandomStringGenerator().nextString());
        ProcessPushCommand command = new ProcessPushCommand();
        command.execute(context, bundle);
        ArgumentCaptor<Bundle> arg = ArgumentCaptor.forClass(Bundle.class);
        verify(pushProcessor, times(1)).processPush(any(Context.class), arg.capture());
        assertThat(arg.getValue()).isEqualToComparingFieldByField(bundle);
    }
}
