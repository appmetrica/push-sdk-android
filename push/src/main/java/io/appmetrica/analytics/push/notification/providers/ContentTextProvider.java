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
 * Extracts value for {@link NotificationCompat.Builder#setContentText(CharSequence)} method.
 */
public class ContentTextProvider implements NotificationValueProvider<Spanned> {

    /**
     * Creates a new instance of {@link ContentTextProvider}.
     */
    public ContentTextProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setContentText(CharSequence)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Spanned get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            String text = notification.getContentText();
            if (!CoreUtils.isEmpty(text)) {
                return Utils.wrapHtml(text);
            }
        }
        return null;
    }
}
