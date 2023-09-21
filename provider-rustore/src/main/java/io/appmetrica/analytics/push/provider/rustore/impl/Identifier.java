package io.appmetrica.analytics.push.provider.rustore.impl;

import android.text.TextUtils;
import androidx.annotation.Nullable;

public class Identifier {

    @Nullable
    final String projectId;

    public Identifier(
        @Nullable final String projectId
    ) {
        this.projectId = projectId;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(projectId) == true;
    }

    public boolean isValid() {
        return TextUtils.isEmpty(projectId) == false;
    }
}
