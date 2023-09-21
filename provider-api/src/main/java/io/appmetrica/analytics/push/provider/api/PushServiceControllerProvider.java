package io.appmetrica.analytics.push.provider.api;

import androidx.annotation.NonNull;

/**
 * Returns implementation of {@link PushServiceController}.
 */
public interface PushServiceControllerProvider {

    /**
     * @return implementation of {@link PushServiceController}
     */
    @NonNull
    PushServiceController getPushServiceController();

}
