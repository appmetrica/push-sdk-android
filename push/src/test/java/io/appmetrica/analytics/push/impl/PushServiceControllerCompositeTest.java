package io.appmetrica.analytics.push.impl;

import android.content.Context;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import java.util.AbstractMap;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushServiceControllerCompositeTest {

    @Rule
    public final MockedStaticRule<PushServiceFacade> sPushServiceFacade =
        new MockedStaticRule<>(PushServiceFacade.class);

    private final String transport1 = "transport#1";
    private final String transport2 = "transport2";
    @Mock
    private PushServiceController controller1;
    @Mock
    private PushServiceController controller2;

    private PushServiceControllerComposite controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(controller1.getTransportId()).thenReturn(transport1);
        when(controller2.getTransportId()).thenReturn(transport2);
        controller = new PushServiceControllerComposite(mock(Context.class), Arrays.asList(controller1, controller2));
    }

    @Test
    public void registerCall() {
        controller.register();
        verify(controller1).register();
        verify(controller2).register();
    }

    @Test
    public void callIfSmthRegistered() {
        doReturn(true).when(controller1).register();
        controller.register();
        sPushServiceFacade.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                PushServiceFacade.initToken(any(Context.class));
            }
        });
    }

    @Test
    public void doNotCallIfNothingRegistered() {
        doReturn(false).when(controller1).register();
        doReturn(false).when(controller2).register();
        controller.register();
        sPushServiceFacade.getStaticMock().verify(() -> PushServiceFacade.initToken(any(Context.class)), never());
    }

    @Test
    public void getAllTokens() {
        String token1 = "token1";
        doReturn(token1).when(controller1).getToken();
        String token2 = "token2";
        doReturn(token2).when(controller2).getToken();

        assertThat(controller.getTokens()).containsOnly(
            new AbstractMap.SimpleEntry<>(transport1, token1),
            new AbstractMap.SimpleEntry<>(transport2, token2)
        );
    }

    @Test
    public void getEmptyToken() {
        String token1 = "token1";
        doReturn(token1).when(controller1).getToken();
        doReturn(null).when(controller2).getToken();

        assertThat(controller.getTokens()).containsOnly(
            new AbstractMap.SimpleEntry<>(transport1, token1),
            new AbstractMap.SimpleEntry<>(transport2, null)
        );
    }
}
