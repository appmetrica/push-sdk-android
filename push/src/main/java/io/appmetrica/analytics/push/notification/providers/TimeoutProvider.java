package io.appmetrica.analytics.push.notification.providers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.utils.AndroidUtils;
import io.appmetrica.analytics.push.impl.utils.PendingIntentFlagHelper;
import io.appmetrica.analytics.push.impl.utils.RequestCodeUtils;
import io.appmetrica.analytics.push.internal.receiver.TtlBroadcastReceiver;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setTimeoutAfter(long)} method.
 */
public class TimeoutProvider implements NotificationValueProvider<Long> {

    private static final String TAG = "[TimeoutProvider]";

    @NonNull
    private final Context context;

    /**
     * Constructor for {@link TimeoutProvider}.
     *
     * @param context this {@link Context} is used to get {@link AlarmManager} is necessary.
     */
    public TimeoutProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setTimeoutAfter(long)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Long get(@NonNull PushMessage pushMessage) {
        Long notificationTtl = null;
        Long timeToHide = null;

        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            notificationTtl = notification.getNotificationTtl();
            timeToHide = notification.getTimeToHideMillis();
        }

        Long realNotificationTtl = null;
        if (notificationTtl != null && timeToHide != null) {
            realNotificationTtl = Math.min(notificationTtl, timeToHide - System.currentTimeMillis());
        } else if (timeToHide != null) {
            realNotificationTtl = timeToHide - System.currentTimeMillis();
        } else {
            realNotificationTtl = notificationTtl;
        }

        if (!AndroidUtils.isApiAchieved(Build.VERSION_CODES.O)) {
            applyNotificationTtlPre26Api(pushMessage, realNotificationTtl);
            realNotificationTtl = null;
        }

        return realNotificationTtl;
    }

    private void applyNotificationTtlPre26Api(
        @NonNull PushMessage pushMessage,
        @Nullable Long notificationTtl
    ) {
        if (notificationTtl != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager == null) {
                DebugLogger.INSTANCE.error(TAG, "Alarm service is not available");
                return;
            }
            final Integer notificationId = pushMessage.getNotification() == null ? null :
                pushMessage.getNotification().getNotificationId();
            final String notificationTag = pushMessage.getNotification() == null ? null :
                pushMessage.getNotification().getNotificationTag();
            final String payload = pushMessage.getPayload();
            Intent intent = new Intent(context, TtlBroadcastReceiver.class)
                .setAction(TtlBroadcastReceiver.EXPIRED_BY_TTL_ACTION)
                .putExtra(Constants.PUSH_ID, pushMessage.getNotificationId())
                .putExtra(Constants.NOTIFICATION_ID, notificationId == null ? 0 : notificationId)
                .putExtra(Constants.NOTIFICATION_TAG, notificationTag)
                .putExtra(Constants.PAYLOAD, payload)
                .putExtra(CoreConstants.EXTRA_TRANSPORT, pushMessage.getTransport());
            int requestCode = RequestCodeUtils.incrementAndGet(context);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntentFlagHelper.getPendingIntentFlag(PendingIntent.FLAG_CANCEL_CURRENT, false)
            );
            alarmManager.set(
                AlarmManager.RTC,
                System.currentTimeMillis() + notificationTtl,
                pendingIntent
            );
        }
    }
}
