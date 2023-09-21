package io.appmetrica.analytics.push.provider.gcm.impl;

import android.text.TextUtils;
import androidx.annotation.Nullable;

public class Identifier {

    @Nullable
    private final String senderId;

    Identifier(@Nullable String senderId) {
        this.senderId = senderId;
    }

    @Nullable
    String getSenderId() {
        return senderId;
    }

    public boolean isValid() {
        return TextUtils.isEmpty(senderId) == false;
    }
}
