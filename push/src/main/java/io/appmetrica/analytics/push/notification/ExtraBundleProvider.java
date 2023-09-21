package io.appmetrica.analytics.push.notification;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Provides extra bundle for notification actions.
 */
public interface ExtraBundleProvider {

    /**
     * Provides extra bundle for notification actions.
     * Method {@link NotificationActionInfo.Builder#withExtraBundle(Bundle)} is called only if return value is not null.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extra bundle if it is required or null otherwise
     */
    @Nullable
    Bundle getExtraBundle(@NonNull PushMessage pushMessage);
}
