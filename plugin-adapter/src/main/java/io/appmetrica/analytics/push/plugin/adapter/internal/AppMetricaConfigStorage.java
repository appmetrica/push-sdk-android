package io.appmetrica.analytics.push.plugin.adapter.internal;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.plugin.adapter.impl.AppMetricaConfigStorageImpl;

public final class AppMetricaConfigStorage {

    private AppMetricaConfigStorage() {}

    public static void saveConfig(@NonNull Context context, @NonNull String configString) {
        AppMetricaConfigStorageImpl.getInstance(context).saveConfig(configString);
    }
}
