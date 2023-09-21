package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

public interface NotificationActionProcessor {

    void processAction(@NonNull final Context context, @NonNull final Intent intent);

    void setClearNotificationProcessingStrategy(@NonNull NotificationActionProcessingStrategy strategy);

    void setOpenActionProcessingStrategy(@NonNull NotificationActionProcessingStrategy strategy);

    void setAdditionalActionProcessingStrategy(@NonNull NotificationActionProcessingStrategy strategy);

    void setInlineActionProcessingStrategy(@NonNull NotificationActionProcessingStrategy strategy);

}
