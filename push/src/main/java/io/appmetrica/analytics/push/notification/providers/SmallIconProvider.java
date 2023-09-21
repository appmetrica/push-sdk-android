package io.appmetrica.analytics.push.notification.providers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setSmallIcon(int)} method.
 */
public class SmallIconProvider implements NotificationValueProvider<Integer> {

    @NonNull
    private final Context context;

    /**
     * Constructor for {@link SmallIconProvider}.
     *
     * @param context this {@link Context} is used to extract from meta data
     */
    public SmallIconProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setSmallIcon(int)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Integer get(@NonNull PushMessage pushMessage) {
        Integer iconResId = null;
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            iconResId = notification.getIconResId();
        }
        if (iconResId == null) {
            iconResId = Utils.getIntegerFromMetaData(context, Constants.DEFAULT_ICON_META_DATA_NAME);
        }
        if (iconResId == null) {
            iconResId = context.getApplicationInfo().icon;
        }
        return iconResId;
    }
}
