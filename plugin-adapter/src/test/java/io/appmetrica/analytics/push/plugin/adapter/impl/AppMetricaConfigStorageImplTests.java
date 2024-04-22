package io.appmetrica.analytics.push.plugin.adapter.impl;

import android.location.Location;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.AppMetricaConfig;
import io.appmetrica.analytics.PreloadInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaConfigStorageImplTests {

    private static final String API_KEY = UUID.randomUUID().toString();

    private static final List<AppMetricaConfig> TEST_CONFIGS = new ArrayList<AppMetricaConfig>() {
        {
            AppMetricaConfig.Builder config = AppMetricaConfig.newConfigBuilder(API_KEY);
            add(config.build());

            add(config.withAppVersion("2.2.2").build());
            add(config.withLogs().build());
            add(config.withLocationTracking(true).build());
            add(config.withCrashReporting(true).build());
            add(config.withNativeCrashReporting(true).build());
            add(config.withSessionTimeout(123).build());

            add(config.withPreloadInfo(PreloadInfo.newBuilder("id-112").build()).build());
            add(config.withPreloadInfo(
                PreloadInfo.newBuilder("id-112")
                    .setAdditionalParams("param1", "value1")
                    .build()
            ).build());

            add(config.withLocation(getTestLocation()).build());
        }
    };

    @Test
    public void testSaveAndLoadConfig() {
        for (int i = 0; i < TEST_CONFIGS.size(); i++) {
            assertConfigEquals(TEST_CONFIGS.get(i));
        }
    }

    private void assertConfigEquals(AppMetricaConfig testConfig) {
        AppMetricaConfigStorageImpl storage = new AppMetricaConfigStorageImpl(RuntimeEnvironment.application);

        storage.saveConfig(testConfig);
        String configJson = storage.loadConfig();

        Assert.assertEquals(testConfig.toJson(), configJson);
    }

    @NonNull
    private static Location getTestLocation() {
        Location location = mock(Location.class);
        doReturn("provider_value").when(location).getProvider();
        doReturn(1.2).when(location).getLatitude();
        doReturn(2.3).when(location).getLongitude();
        doReturn(123L).when(location).getTime();
        doReturn(3.4f).when(location).getAccuracy();
        return location;
    }
}
