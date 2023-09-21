package io.appmetrica.analytics.push.notification.providers;

import android.graphics.Bitmap;
import android.text.Spanned;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setStyle(NotificationCompat.Style)} method.
 */
public class StyleProvider implements NotificationValueProvider<NotificationCompat.Style> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setStyle(NotificationCompat.Style)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public NotificationCompat.Style get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            Bitmap bitmap = notification.getLargeBitmap();
            if (bitmap == null) {
                String contentText = notification.getContentText();
                Spanned wrappedText = null;
                if (contentText != null) {
                    wrappedText = Utils.wrapHtml(contentText);
                }
                return new NotificationCompat.BigTextStyle().bigText(wrappedText);
            } else {
                return new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
            }
        }
        return null;
    }
}
