package io.appmetrica.analytics.push;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.tracking.BaseAppMetricaPushMessageTracker;
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;

/**
 * Main outer facade for tracking push events lifecycle via AppMetrica.
 */
public class AppMetricaPushTracker {

    private final InternalPushMessageTracker pushMessageTracker;

    /**
     * Create the instance of {@link AppMetricaPushTracker}. You can safely create and use several instants of
     * {@link AppMetricaPushTracker}.
     *
     * @param context {@link Context} object. Any application context.
     */
    public AppMetricaPushTracker(@NonNull Context context) {
        this(new BaseAppMetricaPushMessageTracker(AppMetricaPushCore.getInstance(context).getPreferenceManager()));
    }

    AppMetricaPushTracker(@NonNull InternalPushMessageTracker pushMessageTracker) {
        this.pushMessageTracker = pushMessageTracker;
    }

    /**
     * Reports receiving push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * should be activated before it.
     *
     * @param pushId {@link String} unique identifier of push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportReceive(String)
     * @see #reportReceive(Intent)
     * @see #reportReceive(NotificationActionInfo)
     * @see #reportReceive(String, String)
     * @see #reportReceive(Intent, String)
     * @see #reportReceive(NotificationActionInfo, String)
     */
    public void reportReceive(@NonNull String pushId) {
        reportReceive(pushId, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports receiving push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportReceive(String)
     * @see #reportReceive(Intent)
     * @see #reportReceive(NotificationActionInfo)
     * @see #reportReceive(String, String)
     * @see #reportReceive(Intent, String)
     * @see #reportReceive(NotificationActionInfo, String)
     */
    public void reportReceive(@NonNull String pushId, @NonNull String transport) {
        try {
            pushMessageTracker.onMessageReceived(pushId, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report receive for %s", pushId), e);
        }
    }

    /**
     * Reports opening push message. Opening means opening push {@link android.app.Notification} by user or processing
     * push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId {@link String} unique identifier of push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportOpen(String)
     * @see #reportOpen(Intent)
     * @see #reportOpen(NotificationActionInfo)
     * @see #reportOpen(String, String)
     * @see #reportOpen(Intent, String)
     * @see #reportOpen(NotificationActionInfo, String)
     */
    public void reportOpen(@NonNull String pushId) {
        reportOpen(pushId, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports opening push message. Opening means opening push {@link android.app.Notification} by user or processing
     * push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportOpen(String)
     * @see #reportOpen(Intent)
     * @see #reportOpen(NotificationActionInfo)
     * @see #reportOpen(String, String)
     * @see #reportOpen(Intent, String)
     * @see #reportOpen(NotificationActionInfo, String)
     */
    public void reportOpen(@NonNull String pushId, @NonNull String transport) {
        try {
            pushMessageTracker.onPushOpened(pushId, null, transport, null);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report open for %s", pushId), e);
        }
    }

    /**
     * Reports dismissing push message. Dismissing means clearing push {@link android.app.Notification} by user or
     * ignoring push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId {@link String} unique identifier of push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportDismiss(String)
     * @see #reportDismiss(Intent)
     * @see #reportDismiss(NotificationActionInfo)
     * @see #reportDismiss(String, String)
     * @see #reportDismiss(Intent, String)
     * @see #reportDismiss(NotificationActionInfo, String)
     */
    public void reportDismiss(@NonNull String pushId) {
        reportDismiss(pushId, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports dismissing push message. Dismissing means clearing push {@link android.app.Notification} by user or
     * ignoring push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportDismiss(String)
     * @see #reportDismiss(Intent)
     * @see #reportDismiss(NotificationActionInfo)
     * @see #reportDismiss(String, String)
     * @see #reportDismiss(Intent, String)
     * @see #reportDismiss(NotificationActionInfo, String)
     */
    public void reportDismiss(@NonNull String pushId, @NonNull String transport) {
        try {
            pushMessageTracker.onNotificationCleared(pushId, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report dismiss for %s", pushId), e);
        }
    }

    /**
     * Reports additional action. In general, it's additional action of {@link android.app.Notification}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId   {@link String} unique identifier of push message
     * @param actionId {@link String} unique identifier of action
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportAdditionalAction(String, String)
     * @see #reportAdditionalAction(Intent)
     * @see #reportAdditionalAction(NotificationActionInfo)
     * @see #reportAdditionalAction(String, String, String)
     * @see #reportAdditionalAction(Intent, String)
     * @see #reportAdditionalAction(NotificationActionInfo, String)
     */
    public void reportAdditionalAction(@NonNull String pushId, @Nullable String actionId) {
        reportAdditionalAction(pushId, actionId, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports additional action. In general, it's additional action of {@link android.app.Notification}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param actionId  {@link String} unique identifier of action
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportAdditionalAction(String, String)
     * @see #reportAdditionalAction(Intent)
     * @see #reportAdditionalAction(NotificationActionInfo)
     * @see #reportAdditionalAction(String, String, String)
     * @see #reportAdditionalAction(Intent, String)
     * @see #reportAdditionalAction(NotificationActionInfo, String)
     */
    public void reportAdditionalAction(@NonNull String pushId, @Nullable String actionId, @NonNull String transport) {
        try {
            pushMessageTracker.onNotificationAdditionalAction(pushId, actionId, null, transport, null);
        } catch (Throwable e) {
            TrackersHub.getInstance()
                .reportError(String.format("Failed to report additional action for %s", pushId), e);
        }
    }

    /**
     * Reports processing silent push message. Processing means sending push message in application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId {@link String} unique identifier of push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportProcess(String)
     * @see #reportProcess(Intent)
     * @see #reportProcess(NotificationActionInfo)
     * @see #reportProcess(String, String)
     * @see #reportProcess(Intent, String)
     * @see #reportProcess(NotificationActionInfo, String)
     */
    public void reportProcess(@NonNull String pushId) {
        reportProcess(pushId, CoreConstants.Transport.UNKNOWN);
    }


    /**
     * Reports processing silent push message. Processing means sending push message in application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportProcess(String)
     * @see #reportProcess(Intent)
     * @see #reportProcess(NotificationActionInfo)
     * @see #reportProcess(String, String)
     * @see #reportProcess(Intent, String)
     * @see #reportProcess(NotificationActionInfo, String)
     */
    public void reportProcess(@NonNull String pushId, @NonNull String transport) {
        try {
            pushMessageTracker.onSilentPushProcessed(pushId, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report process for %s", pushId), e);
        }
    }

    /**
     * Reports shown push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId {@link String} unique identifier of push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportShown(String)
     * @see #reportShown(Intent)
     * @see #reportShown(NotificationActionInfo)
     * @see #reportShown(String, String)
     * @see #reportShown(Intent, String)
     * @see #reportShown(NotificationActionInfo, String)
     */
    public void reportShown(@NonNull String pushId) {
        reportShown(pushId, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports shown push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportShown(String)
     * @see #reportShown(Intent)
     * @see #reportShown(NotificationActionInfo)
     * @see #reportShown(String, String)
     * @see #reportShown(Intent, String)
     * @see #reportShown(NotificationActionInfo, String)
     */
    public void reportShown(@NonNull String pushId, @NonNull String transport) {
        try {
            pushMessageTracker.onNotificationShown(pushId, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report shown for %s", pushId), e);
        }
    }

    /**
     * Reports ignored push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId   {@link String} unique identifier of push message
     * @param category {@link String} category of the reason for ignoring push message
     * @param details  {@link String} details of the reason for ignoring push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportIgnored(String, String, String)
     * @see #reportIgnored(Intent, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String)
     * @see #reportIgnored(String, String, String, String)
     * @see #reportIgnored(Intent, String, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String, String)
     */
    public void reportIgnored(@NonNull String pushId,
                              @NonNull String category,
                              @NonNull String details) {
        reportIgnored(pushId, category, details, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports ignored push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param category  {@link String} category of the reason for ignoring push message
     * @param details   {@link String} details of the reason for ignoring push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportIgnored(String, String, String)
     * @see #reportIgnored(Intent, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String)
     * @see #reportIgnored(String, String, String, String)
     * @see #reportIgnored(Intent, String, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String, String)
     */
    public void reportIgnored(@NonNull String pushId,
                              @NonNull String category,
                              @NonNull String details,
                              @NonNull String transport) {
        try {
            pushMessageTracker.onNotificationIgnored(pushId, category, details, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report shown for %s", pushId), e);
        }
    }

    /**
     * Reports expired push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId   {@link String} unique identifier of push message
     * @param category {@link String} category of the reason for expiring push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportExpired(String, String)
     * @see #reportExpired(Intent, String)
     * @see #reportExpired(NotificationActionInfo, String)
     * @see #reportExpired(String, String, String)
     * @see #reportExpired(Intent, String, String)
     * @see #reportExpired(NotificationActionInfo, String, String)
     */
    public void reportExpired(@NonNull String pushId,
                              @NonNull String category) {
        reportExpired(pushId, category, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports expired push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param category  {@link String} category of the reason for expiring push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportExpired(String, String)
     * @see #reportExpired(Intent, String)
     * @see #reportExpired(NotificationActionInfo, String)
     * @see #reportExpired(String, String, String)
     * @see #reportExpired(Intent, String, String)
     * @see #reportExpired(NotificationActionInfo, String, String)
     */
    public void reportExpired(@NonNull String pushId,
                              @NonNull String category,
                              @NonNull String transport) {
        try {
            pushMessageTracker.onNotificationExpired(pushId, category, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report shown for %s", pushId), e);
        }
    }

    /**
     * Reports removed push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId   {@link String} unique identifier of push message
     * @param category {@link String} category of the reason for removing push message
     * @param details  {@link String} details of the reason for removing push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportRemoved(String, String, String)
     * @see #reportRemoved(Intent, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String)
     * @see #reportRemoved(String, String, String, String)
     * @see #reportRemoved(Intent, String, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String, String)
     */
    public void reportRemoved(@NonNull String pushId,
                              @NonNull String category,
                              @NonNull String details) {
        reportRemoved(pushId, category, details, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports removed push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param pushId    {@link String} unique identifier of push message
     * @param category  {@link String} category of the reason for removing push message
     * @param details   {@link String} details of the reason for removing push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportRemoved(String, String, String)
     * @see #reportRemoved(Intent, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String)
     * @see #reportRemoved(String, String, String, String)
     * @see #reportRemoved(Intent, String, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String, String)
     */
    public void reportRemoved(@NonNull String pushId,
                              @NonNull String category,
                              @NonNull String details,
                              @NonNull String transport) {
        try {
            pushMessageTracker.onRemovingSilentPushProcessed(pushId, category, details, null, transport);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(String.format("Failed to report shown for %s", pushId), e);
        }
    }

    /**
     * Reports receiving push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportReceive(String)
     * @see #reportReceive(Intent)
     * @see #reportReceive(NotificationActionInfo)
     * @see #reportReceive(String, String)
     * @see #reportReceive(Intent, String)
     * @see #reportReceive(NotificationActionInfo, String)
     */
    public void reportReceive(@Nullable NotificationActionInfo actionInfo) {
        reportReceive(actionInfo, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports receiving push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportReceive(String)
     * @see #reportReceive(Intent)
     * @see #reportReceive(NotificationActionInfo)
     * @see #reportReceive(String, String)
     * @see #reportReceive(Intent, String)
     * @see #reportReceive(NotificationActionInfo, String)
     */
    public void reportReceive(@Nullable NotificationActionInfo actionInfo, @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportReceive(actionInfo.pushId, transport);
        }
    }

    /**
     * Reports opening push message. Opening means opening push {@link android.app.Notification} by user or processing
     * push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportOpen(String)
     * @see #reportOpen(Intent)
     * @see #reportOpen(NotificationActionInfo)
     * @see #reportOpen(String, String)
     * @see #reportOpen(Intent, String)
     * @see #reportOpen(NotificationActionInfo, String)
     */
    public void reportOpen(@Nullable NotificationActionInfo actionInfo) {
        reportOpen(actionInfo, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports opening push message. Opening means opening push {@link android.app.Notification} by user or processing
     * push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportOpen(String)
     * @see #reportOpen(Intent)
     * @see #reportOpen(NotificationActionInfo)
     * @see #reportOpen(String, String)
     * @see #reportOpen(Intent, String)
     * @see #reportOpen(NotificationActionInfo, String)
     */
    public void reportOpen(@Nullable NotificationActionInfo actionInfo, @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportOpen(actionInfo.pushId, transport);
        }
    }

    /**
     * Reports dismissing push message. Dismissing means clearing push {@link android.app.Notification} by user or
     * ignoring push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportDismiss(String)
     * @see #reportDismiss(Intent)
     * @see #reportDismiss(NotificationActionInfo)
     * @see #reportDismiss(String, String)
     * @see #reportDismiss(Intent, String)
     * @see #reportDismiss(NotificationActionInfo, String)
     */
    public void reportDismiss(@Nullable NotificationActionInfo actionInfo) {
        reportDismiss(actionInfo, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports dismissing push message. Dismissing means clearing push {@link android.app.Notification} by user or
     * ignoring push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportDismiss(String)
     * @see #reportDismiss(Intent)
     * @see #reportDismiss(NotificationActionInfo)
     * @see #reportDismiss(String, String)
     * @see #reportDismiss(Intent, String)
     * @see #reportDismiss(NotificationActionInfo, String)
     */
    public void reportDismiss(@Nullable NotificationActionInfo actionInfo, @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportDismiss(actionInfo.pushId, transport);
        }
    }

    /**
     * Reports additional action. In general, it's additional action of {@link android.app.Notification}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportAdditionalAction(String, String)
     * @see #reportAdditionalAction(Intent)
     * @see #reportAdditionalAction(NotificationActionInfo)
     * @see #reportAdditionalAction(String, String, String)
     * @see #reportAdditionalAction(Intent, String)
     * @see #reportAdditionalAction(NotificationActionInfo, String)
     */
    public void reportAdditionalAction(@Nullable NotificationActionInfo actionInfo) {
        reportAdditionalAction(actionInfo, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports additional action. In general, it's additional action of {@link android.app.Notification}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportAdditionalAction(String, String)
     * @see #reportAdditionalAction(Intent)
     * @see #reportAdditionalAction(NotificationActionInfo)
     * @see #reportAdditionalAction(String, String, String)
     * @see #reportAdditionalAction(Intent, String)
     * @see #reportAdditionalAction(NotificationActionInfo, String)
     */
    public void reportAdditionalAction(@Nullable NotificationActionInfo actionInfo, @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportAdditionalAction(actionInfo.pushId, actionInfo.actionId, transport);
        }
    }

    /**
     * Reports processing silent push message. Processing means sending push message in application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportProcess(String)
     * @see #reportProcess(Intent)
     * @see #reportProcess(NotificationActionInfo)
     * @see #reportProcess(String, String)
     * @see #reportProcess(Intent, String)
     * @see #reportProcess(NotificationActionInfo, String)
     */
    public void reportProcess(@Nullable NotificationActionInfo actionInfo) {
        reportProcess(actionInfo, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports processing silent push message. Processing means sending push message in application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportProcess(String)
     * @see #reportProcess(Intent)
     * @see #reportProcess(NotificationActionInfo)
     * @see #reportProcess(String, String)
     * @see #reportProcess(Intent, String)
     * @see #reportProcess(NotificationActionInfo, String)
     */
    public void reportProcess(@Nullable NotificationActionInfo actionInfo, @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportProcess(actionInfo.pushId, transport);
        }
    }

    /**
     * Reports shown push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportShown(String)
     * @see #reportShown(Intent)
     * @see #reportShown(NotificationActionInfo)
     * @see #reportShown(String, String)
     * @see #reportShown(Intent, String)
     * @see #reportShown(NotificationActionInfo, String)
     */
    public void reportShown(@Nullable NotificationActionInfo actionInfo) {
        reportShown(actionInfo, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports shown push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportShown(String)
     * @see #reportShown(Intent)
     * @see #reportShown(NotificationActionInfo)
     * @see #reportShown(String, String)
     * @see #reportShown(Intent, String)
     * @see #reportShown(NotificationActionInfo, String)
     */
    public void reportShown(@Nullable NotificationActionInfo actionInfo, @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportShown(actionInfo.pushId, transport);
        }
    }

    /**
     * Reports ignored push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param category   {@link String} category of the reason for ignoring push message
     * @param details    {@link String} details of the reason for ignoring push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportIgnored(String, String, String)
     * @see #reportIgnored(Intent, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String)
     * @see #reportIgnored(String, String, String, String)
     * @see #reportIgnored(Intent, String, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String, String)
     */
    public void reportIgnored(@Nullable NotificationActionInfo actionInfo,
                              @NonNull String category,
                              @NonNull String details) {
        reportIgnored(actionInfo, category, details, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports ignored push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param category   {@link String} category of the reason for ignoring push message
     * @param details    {@link String} details of the reason for ignoring push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportIgnored(String, String, String)
     * @see #reportIgnored(Intent, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String)
     * @see #reportIgnored(String, String, String, String)
     * @see #reportIgnored(Intent, String, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String, String)
     */
    public void reportIgnored(@Nullable NotificationActionInfo actionInfo,
                              @NonNull String category,
                              @NonNull String details,
                              @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportIgnored(actionInfo.pushId, category, details, transport);
        }
    }

    /**
     * Reports expired push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param category   {@link String} category of the reason for expiring push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportExpired(String, String)
     * @see #reportExpired(Intent, String)
     * @see #reportExpired(NotificationActionInfo, String)
     * @see #reportExpired(String, String, String)
     * @see #reportExpired(Intent, String, String)
     * @see #reportExpired(NotificationActionInfo, String, String)
     */
    public void reportExpired(@Nullable NotificationActionInfo actionInfo,
                              @NonNull String category) {
        reportExpired(actionInfo, category, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports expired push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param category   {@link String} category of the reason for expiring push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportExpired(String, String)
     * @see #reportExpired(Intent, String)
     * @see #reportExpired(NotificationActionInfo, String)
     * @see #reportExpired(String, String, String)
     * @see #reportExpired(Intent, String, String)
     * @see #reportExpired(NotificationActionInfo, String, String)
     */
    public void reportExpired(@Nullable NotificationActionInfo actionInfo,
                              @NonNull String category,
                              @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportExpired(actionInfo.pushId, category, transport);
        }
    }

    /**
     * Reports removed push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param category   {@link String} category of the reason for removing push message
     * @param details    {@link String} details of the reason for removing push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportRemoved(String, String, String)
     * @see #reportRemoved(Intent, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String)
     * @see #reportRemoved(String, String, String, String)
     * @see #reportRemoved(Intent, String, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String, String)
     */
    public void reportRemoved(@Nullable NotificationActionInfo actionInfo,
                              @NonNull String category,
                              @NonNull String details) {
        reportRemoved(actionInfo, category, details, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports removed push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param actionInfo {@link NotificationActionInfo} for push message
     * @param category   {@link String} category of the reason for removing push message
     * @param details    {@link String} details of the reason for removing push message
     * @param transport  {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportRemoved(String, String, String)
     * @see #reportRemoved(Intent, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String)
     * @see #reportRemoved(String, String, String, String)
     * @see #reportRemoved(Intent, String, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String, String)
     */
    public void reportRemoved(@Nullable NotificationActionInfo actionInfo,
                              @NonNull String category,
                              @NonNull String details,
                              @NonNull String transport) {
        if (actionInfo != null && actionInfo.pushId != null) {
            reportRemoved(actionInfo.pushId, category, details, transport);
        }
    }

    /**
     * Reports receiving push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * should be activated before it.
     *
     * @param intent {@link Intent} Intent for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportReceive(String)
     * @see #reportReceive(Intent)
     * @see #reportReceive(NotificationActionInfo)
     * @see #reportReceive(String, String)
     * @see #reportReceive(Intent, String)
     * @see #reportReceive(NotificationActionInfo, String)
     */
    public void reportReceive(@Nullable Intent intent) {
        reportReceive(intent, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports receiving push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportReceive(String)
     * @see #reportReceive(Intent)
     * @see #reportReceive(NotificationActionInfo)
     * @see #reportReceive(String, String)
     * @see #reportReceive(Intent, String)
     * @see #reportReceive(NotificationActionInfo, String)
     */
    public void reportReceive(@Nullable Intent intent, @NonNull String transport) {
        reportReceive(getActionInfoFromIntent(intent), transport);
    }

    /**
     * Reports opening push message. Opening means opening push {@link android.app.Notification} by user or processing
     * push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent {@link Intent} Intent for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportOpen(String)
     * @see #reportOpen(Intent)
     * @see #reportOpen(NotificationActionInfo)
     * @see #reportOpen(String, String)
     * @see #reportOpen(Intent, String)
     * @see #reportOpen(NotificationActionInfo, String)
     */
    public void reportOpen(@Nullable Intent intent) {
        reportOpen(intent, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports opening push message. Opening means opening push {@link android.app.Notification} by user or processing
     * push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportOpen(String)
     * @see #reportOpen(Intent)
     * @see #reportOpen(NotificationActionInfo)
     * @see #reportOpen(String, String)
     * @see #reportOpen(Intent, String)
     * @see #reportOpen(NotificationActionInfo, String)
     */
    public void reportOpen(@Nullable Intent intent, @NonNull String transport) {
        reportOpen(getActionInfoFromIntent(intent), transport);
    }

    /**
     * Reports dismissing push message. Dismissing means clearing push {@link android.app.Notification} by user or
     * ignoring push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent {@link Intent} Intent for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportDismiss(String)
     * @see #reportDismiss(Intent)
     * @see #reportDismiss(NotificationActionInfo)
     * @see #reportDismiss(String, String)
     * @see #reportDismiss(Intent, String)
     * @see #reportDismiss(NotificationActionInfo, String)
     */
    public void reportDismiss(@Nullable Intent intent) {
        reportDismiss(intent, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports dismissing push message. Dismissing means clearing push {@link android.app.Notification} by user or
     * ignoring push message by application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportDismiss(String)
     * @see #reportDismiss(Intent)
     * @see #reportDismiss(NotificationActionInfo)
     * @see #reportDismiss(String, String)
     * @see #reportDismiss(Intent, String)
     * @see #reportDismiss(NotificationActionInfo, String)
     */
    public void reportDismiss(@Nullable Intent intent, @NonNull String transport) {
        reportDismiss(getActionInfoFromIntent(intent), transport);
    }

    /**
     * Reports additional action. In general, it's additional action of {@link android.app.Notification}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent {@link Intent} Intent for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportAdditionalAction(String, String)
     * @see #reportAdditionalAction(Intent)
     * @see #reportAdditionalAction(NotificationActionInfo)
     * @see #reportAdditionalAction(String, String, String)
     * @see #reportAdditionalAction(Intent, String)
     * @see #reportAdditionalAction(NotificationActionInfo, String)
     */
    public void reportAdditionalAction(@Nullable Intent intent) {
        reportAdditionalAction(intent, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports additional action. In general, it's additional action of {@link android.app.Notification}.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportAdditionalAction(String, String)
     * @see #reportAdditionalAction(Intent)
     * @see #reportAdditionalAction(NotificationActionInfo)
     * @see #reportAdditionalAction(String, String, String)
     * @see #reportAdditionalAction(Intent, String)
     * @see #reportAdditionalAction(NotificationActionInfo, String)
     */
    public void reportAdditionalAction(@Nullable Intent intent, @NonNull String transport) {
        reportAdditionalAction(getActionInfoFromIntent(intent), transport);
    }

    /**
     * Reports processing silent push message. Processing means sending push message in application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent {@link Intent} Intent for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportProcess(String)
     * @see #reportProcess(Intent)
     * @see #reportProcess(NotificationActionInfo)
     * @see #reportProcess(String, String)
     * @see #reportProcess(Intent, String)
     * @see #reportProcess(NotificationActionInfo, String)
     */
    public void reportProcess(@Nullable Intent intent) {
        reportProcess(intent, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports processing silent push message. Processing means sending push message in application.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportProcess(String)
     * @see #reportProcess(Intent)
     * @see #reportProcess(NotificationActionInfo)
     * @see #reportProcess(String, String)
     * @see #reportProcess(Intent, String)
     * @see #reportProcess(NotificationActionInfo, String)
     */
    public void reportProcess(@Nullable Intent intent, @NonNull String transport) {
        reportProcess(getActionInfoFromIntent(intent), transport);
    }

    /**
     * Reports shown push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent {@link Intent} Intent for push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportShown(String)
     * @see #reportShown(Intent)
     * @see #reportShown(NotificationActionInfo)
     * @see #reportShown(String, String)
     * @see #reportShown(Intent, String)
     * @see #reportShown(NotificationActionInfo, String)
     */
    public void reportShown(@Nullable Intent intent) {
        reportShown(intent, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports shown push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportShown(String)
     * @see #reportShown(Intent)
     * @see #reportShown(NotificationActionInfo)
     * @see #reportShown(String, String)
     * @see #reportShown(Intent, String)
     * @see #reportShown(NotificationActionInfo, String)
     */
    public void reportShown(@Nullable Intent intent, @NonNull String transport) {
        reportShown(getActionInfoFromIntent(intent), transport);
    }

    /**
     * Reports ignored push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent   {@link Intent} Intent for push message
     * @param category {@link String} category of the reason for ignoring push message
     * @param details  {@link String} details of the reason for ignoring push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportIgnored(String, String, String)
     * @see #reportIgnored(Intent, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String)
     * @see #reportIgnored(String, String, String, String)
     * @see #reportIgnored(Intent, String, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String, String)
     */
    public void reportIgnored(@Nullable Intent intent,
                              @NonNull String category,
                              @NonNull String details) {
        reportIgnored(intent, category, details, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports ignored push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param category  {@link String} category of the reason for ignoring push message
     * @param details   {@link String} details of the reason for ignoring push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportIgnored(String, String, String)
     * @see #reportIgnored(Intent, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String)
     * @see #reportIgnored(String, String, String, String)
     * @see #reportIgnored(Intent, String, String, String)
     * @see #reportIgnored(NotificationActionInfo, String, String, String)
     */
    public void reportIgnored(@Nullable Intent intent,
                              @NonNull String category,
                              @NonNull String details,
                              @NonNull String transport) {
        reportIgnored(getActionInfoFromIntent(intent), category, details, transport);
    }

    /**
     * Reports expired push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent   {@link Intent} Intent for push message
     * @param category {@link String} category of the reason for expiring push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportExpired(String, String)
     * @see #reportExpired(Intent, String)
     * @see #reportExpired(NotificationActionInfo, String)
     * @see #reportExpired(String, String, String)
     * @see #reportExpired(Intent, String, String)
     * @see #reportExpired(NotificationActionInfo, String, String)
     */
    public void reportExpired(@Nullable Intent intent,
                              @NonNull String category) {
        reportExpired(intent, category, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports expired push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param category  {@link String} category of the reason for expiring push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportExpired(String, String)
     * @see #reportExpired(Intent, String)
     * @see #reportExpired(NotificationActionInfo, String)
     * @see #reportExpired(String, String, String)
     * @see #reportExpired(Intent, String, String)
     * @see #reportExpired(NotificationActionInfo, String, String)
     */
    public void reportExpired(@Nullable Intent intent,
                              @NonNull String category,
                              @NonNull String transport) {
        reportExpired(getActionInfoFromIntent(intent), category, transport);
    }

    /**
     * Reports removed push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent   {@link Intent} Intent for push message
     * @param category {@link String} category of the reason for removing push message
     * @param details  {@link String} details of the reason for removing push message
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportRemoved(String, String, String)
     * @see #reportRemoved(Intent, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String)
     * @see #reportRemoved(String, String, String, String)
     * @see #reportRemoved(Intent, String, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String, String)
     */
    public void reportRemoved(@Nullable Intent intent,
                              @NonNull String category,
                              @NonNull String details) {
        reportRemoved(intent, category, details, CoreConstants.Transport.UNKNOWN);
    }

    /**
     * Reports removed push message.
     * <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a> should be activated before it.
     *
     * @param intent    {@link Intent} Intent for push message
     * @param category  {@link String} category of the reason for removing push message
     * @param details   {@link String} details of the reason for removing push message
     * @param transport {@link String} transport push message was received from
     * @see <a href = "https://appmetrica.io/docs/mobile-sdk-dg/concepts/android-initialize.html">
     * AppMetrica SDK </a>
     * @see #reportRemoved(String, String, String)
     * @see #reportRemoved(Intent, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String)
     * @see #reportRemoved(String, String, String, String)
     * @see #reportRemoved(Intent, String, String, String)
     * @see #reportRemoved(NotificationActionInfo, String, String, String)
     */
    public void reportRemoved(@Nullable Intent intent,
                              @NonNull String category,
                              @NonNull String details,
                              @NonNull String transport) {
        reportRemoved(getActionInfoFromIntent(intent), category, details, transport);
    }

    @Nullable
    private NotificationActionInfo getActionInfoFromIntent(@Nullable Intent intent) {
        if (intent != null) {
            return intent.getParcelableExtra(AppMetricaPush.EXTRA_ACTION_INFO);
        }
        return null;
    }
}
