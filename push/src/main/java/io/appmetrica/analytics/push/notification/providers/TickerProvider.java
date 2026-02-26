package io.appmetrica.analytics.push.notification.providers;

import android.text.Spanned;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setTicker(CharSequence)} method.
 */
public class TickerProvider implements NotificationValueProvider<Spanned> {

    /**
     * Creates a new instance of {@link TickerProvider}.
     */
    public TickerProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setTicker(CharSequence)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Spanned get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        String ticker = null;
        if (notification != null) {
            ticker = notification.getTicker();
        }
        if (!CoreUtils.isEmpty(ticker)) {
            return Utils.wrapHtml(ticker);
        }
        return null;
    }
}
