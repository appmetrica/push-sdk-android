package io.appmetrica.analytics.push.plugin.adapter.impl;

import android.content.Context;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.AppMetricaConfig;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class InitializerTests {

    @Rule
    public final MockedStaticRule<AppMetrica> appMetricaMockedStaticRule =
        new MockedStaticRule<>(AppMetrica.class);
    @Rule
    public final MockedStaticRule<AppMetricaPush> appMetricaPushMockedStaticRule =
        new MockedStaticRule<>(AppMetricaPush.class);
    @Rule
    public final MockedStaticRule<AppMetricaConfigStorageImpl> configStorage =
        new MockedStaticRule<>(AppMetricaConfigStorageImpl.class);
    @Rule
    public final MockedStaticRule<ModulesFacade> modulesFacadeMockedStaticRule =
        new MockedStaticRule<>(ModulesFacade.class);

    private final Initializer initializer = new Initializer(RuntimeEnvironment.application);

    @Test
    public void testInitAppMetricaWithoutConfig() {
        when(ModulesFacade.isActivatedForApp()).thenReturn(false);
        configStorage.getStaticMock().when(new MockedStatic.Verification() {
            @Override
            public void apply() {
                AppMetricaConfigStorageImpl.getInstance(any(Context.class));
            }
        }).thenReturn(mock(AppMetricaConfigStorageImpl.class));
        initializer.initIfNeeded();
        appMetricaMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() {
                AppMetrica.activate(any(Context.class), any(AppMetricaConfig.class));
            }
        }, never());
    }

    @Test
    public void testInitAppMetricaWithConfig() {
        when(ModulesFacade.isActivatedForApp()).thenReturn(false);
        final String apiKey = prepareConfig();

        initializer.initIfNeeded();

        appMetricaMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() {
                AppMetrica.activate(any(Context.class), argThat(new ArgumentMatcher<AppMetricaConfig>() {
                    @Override
                    public boolean matches(AppMetricaConfig argument) {
                        return apiKey.equals(argument.apiKey);
                    }
                }));
            }
        });
    }

    @Test
    public void testInitAppMetricaIfActivated() {
        when(ModulesFacade.isActivatedForApp()).thenReturn(true);

        initializer.initIfNeeded();

        appMetricaMockedStaticRule.getStaticMock().verifyNoInteractions();
    }

    @Test
    public void testInitTwice() {
        when(ModulesFacade.isActivatedForApp()).thenReturn(false);
        final String apiKey = prepareConfig();

        initializer.initIfNeeded();

        appMetricaMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() {
                AppMetrica.activate(any(Context.class), argThat(new ArgumentMatcher<AppMetricaConfig>() {
                    @Override
                    public boolean matches(AppMetricaConfig argument) {
                        return apiKey.equals(argument.apiKey);
                    }
                }));
            }
        });

        appMetricaMockedStaticRule.getStaticMock().reset();
        appMetricaPushMockedStaticRule.getStaticMock().reset();

        initializer.initIfNeeded();
        appMetricaMockedStaticRule.getStaticMock().verifyNoInteractions();
        appMetricaPushMockedStaticRule.getStaticMock().verifyNoInteractions();
    }

    @Test
    public void testInitAppMetricaPush() {
        when(ModulesFacade.isActivatedForApp()).thenReturn(false);
        prepareConfig();

        initializer.initIfNeeded();

        appMetricaPushMockedStaticRule.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() {
                AppMetricaPush.activate(any(Context.class));
            }
        });
    }

    private String prepareConfig() {
        final String apiKey = UUID.randomUUID().toString();
        AppMetricaConfig config = AppMetricaConfig.newConfigBuilder(apiKey).build();
        AppMetricaConfigStorageImpl storage = mock(AppMetricaConfigStorageImpl.class);
        configStorage.getStaticMock().when(new MockedStatic.Verification() {
            @Override
            public void apply() {
                AppMetricaConfigStorageImpl.getInstance(any(Context.class));
            }
        }).thenReturn(storage);
        doReturn(config.toJson()).when(storage).loadConfig();
        return apiKey;
    }
}
