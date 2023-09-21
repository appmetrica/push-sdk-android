package io.appmetrica.analytics.push.settings;

import android.content.Context;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;

/**
 * Provider for {@link AppMetricaPush#setPassportUidProvider(Context, PassportUidProvider)}.
 */
public interface PassportUidProvider {

    /**
     * Called when AppMetrica Push SDK wants to get passport uid.
     *
     * @return null if no account, else actual passport uid from AccountManager
     */
    @Nullable
    String getUid();
}
