package io.appmetrica.analytics.push.provider.hms.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.provider.api.PushServiceController;

public class DummyPushServiceController implements PushServiceController {

    @Override
    public boolean register() {
        return false;
    }

    @Nullable
    @Override
    public String getToken() {
        return null;
    }

    @NonNull
    @Override
    public String getTitle() {
        return CoreConstants.Transport.HMS;
    }
}
