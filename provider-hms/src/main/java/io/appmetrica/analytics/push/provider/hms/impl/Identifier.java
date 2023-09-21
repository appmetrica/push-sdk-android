package io.appmetrica.analytics.push.provider.hms.impl;

import android.text.TextUtils;
import androidx.annotation.Nullable;

public class Identifier {

    @Nullable
    private final String appId;

    Identifier(@Nullable String appId) {
        this.appId = appId;
    }

    @Nullable
    String getAppId() {
        return appId;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(appId);
    }

    public boolean isValid() {
        return TextUtils.isEmpty(appId) == false;
    }
}
