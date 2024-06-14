package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.PreferenceManager;

public class AppMetricaPushMessageTracker extends BaseAppMetricaPushMessageTracker {

    public AppMetricaPushMessageTracker(@NonNull PreferenceManager preferenceManager) {
        super(preferenceManager);
    }

    @Override
    public void onPushTokenInited(@NonNull final String value, @NonNull String transport) {
        try {
            super.onPushTokenInited(value, transport);
        } catch (Exception e) {
            String msg = "Try to send PushTokenInited message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onPushTokenUpdated(@NonNull final String value, @NonNull String transport) {
        try {
            super.onPushTokenUpdated(value, transport);
        } catch (Exception e) {
            String msg = "Try to send PushTokenUpdated message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onMessageReceived(@NonNull final String pushId,
                                  @Nullable final String payload,
                                  @NonNull String transport) {
        try {
            super.onMessageReceived(pushId, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send MessageReceived message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationCleared(@NonNull final String pushId,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        try {
            super.onNotificationCleared(pushId, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationCleared message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onPushOpened(@NonNull final String pushId,
                             @Nullable final String payload,
                             @NonNull String transport) {
        try {
            super.onPushOpened(pushId, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send PushOpened message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationAdditionalAction(@NonNull final String pushId,
                                               @Nullable final String actionId,
                                               @Nullable final String payload,
                                               @NonNull String transport) {
        try {
            super.onNotificationAdditionalAction(pushId, actionId, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationAdditionalAction message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationInlineAdditionalAction(@NonNull final String pushId,
                                                     @Nullable final String actionId,
                                                     @Nullable final String payload,
                                                     @NonNull final String text,
                                                     @NonNull String transport) {
        try {
            super.onNotificationInlineAdditionalAction(pushId, actionId, payload, text, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationInlineAdditionalAction message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onSilentPushProcessed(@NonNull final String pushId,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        try {
            super.onSilentPushProcessed(pushId, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send SilentPushProcessed message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationShown(@NonNull String pushId,
                                    @Nullable final String payload,
                                    @NonNull String transport) {
        try {
            super.onNotificationShown(pushId, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationShown message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationIgnored(@NonNull final String pushId,
                                      @Nullable final String category,
                                      @Nullable final String details,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        try {
            super.onNotificationIgnored(pushId, category, details, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationIgnored message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationExpired(@NonNull final String pushId,
                                      @Nullable String category,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        try {
            super.onNotificationExpired(pushId, category, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationTtl message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onRemovingSilentPushProcessed(@NonNull final String pushId,
                                              @Nullable final String category,
                                              @Nullable final String details,
                                              @Nullable final String payload,
                                              @NonNull String transport) {
        try {
            super.onRemovingSilentPushProcessed(pushId, category, details, payload, transport);
        } catch (Exception e) {
            String msg = "Try to send RemovingSilentPushProcessed message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }

    @Override
    public void onNotificationReplace(@NonNull String pushId, @Nullable String newPushId, @NonNull String transport) {
        try {
            super.onNotificationReplace(pushId, newPushId, transport);
        } catch (Exception e) {
            String msg = "Try to send NotificationHide message before appmetrica activation";
            TrackersHub.getInstance().reportError(msg, e);
            PLog.w(msg);
        }
    }
}
