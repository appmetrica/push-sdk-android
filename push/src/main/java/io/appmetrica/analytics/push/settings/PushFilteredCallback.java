package io.appmetrica.analytics.push.settings;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Interface for custom {@link PushFilteredCallback}.
 * Callback can be set via {@link AppMetricaPush#addPushFilteredCallback(Context, PushFilteredCallback)} method.
 */
public interface PushFilteredCallback {

    /**
     * Called if {@link PushMessage} if filtered.
     *
     * @param pushMessage received {@link PushMessage}
     * @param filterResult reason of filtration
     */
    void onPushFiltered(@NonNull final PushMessage pushMessage,
                        @NonNull final PushFilter.FilterResult filterResult);
}
