package io.appmetrica.analytics.push.impl.notification.processing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.utils.AndroidUtils;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.concurrent.TimeUnit;

public class InlineActionProcessingStrategy implements NotificationActionProcessingStrategy {

    public static final String KEY_TEXT_REPLY = "key_text_reply";

    @Override
    public void doAction(@NonNull final Context context, @NonNull final Intent intent) {
        final NotificationActionInfo actionInfo = intent.getParcelableExtra(AppMetricaPush.EXTRA_ACTION_INFO);
        final Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (actionInfo != null && remoteInput != null) {
            final String input = remoteInput.getCharSequence(KEY_TEXT_REPLY, "").toString();
            PublicLogger.INSTANCE.info("Received inline input from action %s with text %s",
                actionInfo.actionId, input);
            sendMessageToAppMetrica(context, actionInfo, input);
            sendBroadcast(context, actionInfo, input);
            removeNotification(context, actionInfo, input);
        }
    }

    private void sendMessageToAppMetrica(@NonNull final Context context,
                                         @NonNull final NotificationActionInfo actionInfo,
                                         @NonNull final String input) {
        if (!CoreUtils.isEmpty(actionInfo.pushId)) {
            AppMetricaPushCore.getInstance(context).getPushServiceProvider().getPushMessageTracker()
                .onNotificationInlineAdditionalAction(
                    actionInfo.pushId,
                    actionInfo.actionId,
                    actionInfo.payload,
                    input,
                    actionInfo.transport
                );
        }
    }

    private void sendBroadcast(@NonNull final Context context,
                               @NonNull final NotificationActionInfo actionInfo,
                               @NonNull final String input) {
        final String action = context.getPackageName() + Constants.INLINE_PUSH_POSTFIX;
        context.sendBroadcast(new Intent(action)
            .setPackage(context.getPackageName())
            .putExtra(AppMetricaPush.EXTRA_ACTION_INFO, actionInfo)
            .putExtra(Constants.INLINE_ACTION_REPLY, input)
        );
    }

    private void removeNotification(@NonNull final Context context,
                                    @NonNull final NotificationActionInfo actionInfo,
                                    @NonNull final String input) {
        final NotificationManager notificationManager =
            (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.P)) {
                final Notification repliedNotification =
                    new NotificationCompat.Builder(context, actionInfo.channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentText(input)
                        .setTimeoutAfter(TimeUnit.SECONDS.toMillis(actionInfo.hideAfterSeconds))
                        .build();
                notificationManager.notify(actionInfo.notificationTag, actionInfo.notificationId, repliedNotification);
            } else {
                notificationManager.cancel(actionInfo.notificationTag, actionInfo.notificationId);
                AppMetricaPushCore.getInstance(context).getPushMessageHistory().setPushActive(actionInfo.pushId, false);
            }
        }
    }
}
