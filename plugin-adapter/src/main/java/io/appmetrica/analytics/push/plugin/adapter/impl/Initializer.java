package io.appmetrica.analytics.push.plugin.adapter.impl;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.AppMetricaConfig;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.push.AppMetricaPush;

public class Initializer {

    @NonNull
    private final Context appContext;

    private boolean initialized = false;

    public Initializer(@NonNull final Context context) {
        appContext = context.getApplicationContext();
    }

    public synchronized boolean initIfNeeded() {
        if (!initialized && initAppMetrica()) {
            initialized = true;
            initAppMetricaPush();
        }
        return initialized;
    }

    private synchronized boolean initAppMetrica() {
        if (!ModulesFacade.isActivatedForApp()) {
            final String configJson = AppMetricaConfigStorageImpl.getInstance(appContext).loadConfig();
            if (TextUtils.isEmpty(configJson) == false) {
                final AppMetricaConfig config = AppMetricaConfig.fromJson(configJson);
                if (config != null) {
                    AppMetrica.activate(appContext, config);
                    return true;
                }
            }
        }
        return false;
    }

    private void initAppMetricaPush() {
        AppMetricaPush.activate(appContext);
    }
}
