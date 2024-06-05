package io.appmetrica.analytics.push.provider.hms.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions;

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
    public String getTransportId() {
        return CoreConstants.Transport.HMS;
    }

    @NonNull
    @Override
    public PushServiceExecutionRestrictions getExecutionRestrictions() {
        return new PushServiceExecutionRestrictions() {

            @Override
            public Long getMaxTaskExecutionDurationSeconds() {
                return super.getMaxTaskExecutionDurationSeconds();
            }
        };
    }
}
