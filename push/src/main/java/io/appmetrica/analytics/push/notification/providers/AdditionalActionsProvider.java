package io.appmetrica.analytics.push.notification.providers;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.notification.processing.InlineActionProcessingStrategy;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.internal.IntentHelper;
import io.appmetrica.analytics.push.model.AdditionalAction;
import io.appmetrica.analytics.push.model.AdditionalActionType;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts value for {@link NotificationCompat.Builder#addAction(NotificationCompat.Action)} method.
 */
public class AdditionalActionsProvider implements NotificationValueProvider<List<NotificationCompat.Action>> {

    @NonNull
    private final Context context;

    /**
     * Constructor for {@link AdditionalActionsProvider}.
     *
     * @param context this {@link Context} is used to create {@link PendingIntent}
     */
    public AdditionalActionsProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#addAction(NotificationCompat.Action)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public List<NotificationCompat.Action> get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification == null) {
            return null;
        }
        AdditionalAction[] additionalActions = notification.getAdditionalActions();
        if (additionalActions == null) {
            return null;
        }
        List<NotificationCompat.Action> result = new ArrayList<>();
        for (AdditionalAction additionalAction : additionalActions) {
            if (CoreUtils.isNotEmpty(additionalAction.getTitle())) {
                final NotificationActionInfo actionInfo =
                    IntentHelper.createNotificationActionInfo(pushMessage, additionalAction);
                final PendingIntent actionIntent =
                    IntentHelper.getPendingIntentForAdditionalAction(context, additionalAction, actionInfo);
                final int iconResId = additionalAction.getIconResId() == null ? 0 : additionalAction.getIconResId();
                final NotificationCompat.Action.Builder replyActionBuilder =
                    new NotificationCompat.Action.Builder(iconResId, additionalAction.getTitle(), actionIntent);
                boolean needButton = true;
                if (additionalAction.getType() == AdditionalActionType.INLINE) {
                    if (Utils.isApiAchived(Build.VERSION_CODES.N)
                        && CoreUtils.isNotEmpty(additionalAction.getLabel())) {
                        final RemoteInput remoteInput =
                            new RemoteInput.Builder(InlineActionProcessingStrategy.KEY_TEXT_REPLY)
                                .setLabel(additionalAction.getLabel())
                                .build();
                        replyActionBuilder.addRemoteInput(remoteInput);
                    } else {
                        needButton = false;
                    }
                }
                if (needButton) {
                    result.add(replyActionBuilder.build());
                }
            }
        }
        return result;
    }
}
