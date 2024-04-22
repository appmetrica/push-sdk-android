package io.appmetrica.analytics.push.plugin.adapter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.AppMetricaConfig;

public class AppMetricaConfigStorageImpl {

    private static final String FILE_NAME = "amp_plugin";
    private static final String KEY_PREFS_CONFIG = "amp_metrica_config";

    @NonNull
    private final SharedPreferences sharedPreferences;

    private static final Object INIT_MONITOR = new Object();

    private volatile static AppMetricaConfigStorageImpl instance;

    public static AppMetricaConfigStorageImpl getInstance(@NonNull final Context context) {
        if (instance == null) {
            synchronized (INIT_MONITOR) {
                if (instance == null) {
                    instance = new AppMetricaConfigStorageImpl(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @VisibleForTesting
    AppMetricaConfigStorageImpl(@NonNull final Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveConfig(@NonNull final AppMetricaConfig config) {
        sharedPreferences.edit().putString(KEY_PREFS_CONFIG, config.toJson()).commit();
    }

    public void saveConfig(@NonNull final String config) {
        sharedPreferences.edit().putString(KEY_PREFS_CONFIG, config).commit();
    }

    @Nullable
    public String loadConfig() {
        return sharedPreferences.getString(KEY_PREFS_CONFIG, null);
    }
}
