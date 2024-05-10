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

@RunWith(RobolectricTestRunner.class)
public class PushServiceControllerCompositeTest {

    @Rule
    public final MockedStaticRule<PushServiceFacade> sPushServiceFacade =
        new MockedStaticRule<>(PushServiceFacade.class);

    @Mock
    private PushServiceController controller1;
    @Mock
    private PushServiceController controller2;

    private PushServiceControllerComposite controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
        sPushServiceFacade.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                PushServiceFacade.initToken(any(Context.class));
            }
        }, never());
    }

    @Test
    public void getAllTokens() {
        String token1 = "token1";
        String title1 = "title1";
        doReturn(token1).when(controller1).getToken();
        doReturn(title1).when(controller1).getTitle();
        String token2 = "token2";
        String title2 = "title2";
        doReturn(token2).when(controller2).getToken();
        doReturn(title2).when(controller2).getTitle();

        assertThat(controller.getTokens()).containsOnly(
            new AbstractMap.SimpleEntry<String, String>(title1, token1),
            new AbstractMap.SimpleEntry<String, String>(title2, token2)
        );
    }

    @Test
    public void getEmptyToken() {
        String token1 = "token1";
        String title1 = "title1";
        doReturn(token1).when(controller1).getToken();
        doReturn(title1).when(controller1).getTitle();
        String title2 = "title2";
        doReturn(null).when(controller2).getToken();
        doReturn(title2).when(controller2).getTitle();

        assertThat(controller.getTokens()).containsOnly(
            new AbstractMap.SimpleEntry<String, String>(title1, token1),
            new AbstractMap.SimpleEntry<String, String>(title2, null)
        );
    }
}
