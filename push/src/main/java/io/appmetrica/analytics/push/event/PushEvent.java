package io.appmetrica.analytics.push.event;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.impl.event.IntentToPushEventConverter;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;

/**
 * Abstract base class for all push events.
 * Provides factory methods for creating various types of push events.
 *
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 */
public abstract class PushEvent {

    /**
     * Creates a new instance of {@link PushEvent}.
     */
    public PushEvent() {
    }

    /**
     * Creates an event representing additional action call.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * AdditionalActionPushEvent event = PushEvent.additionalActionEvent(pushId, actionId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @param actionId the ID of the action performed
     * @return a new {@link AdditionalActionPushEvent} instance
     */
    @NonNull
    public static AdditionalActionPushEvent additionalActionEvent(
        @NonNull String pushId,
        @Nullable String actionId
    ) {
        return new AdditionalActionPushEvent(
            pushId
        ).withActionId(actionId);
    }

    /**
     * Creates an event representing dismiss/clear of a push notification from the notification center.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * DismissPushEvent event = PushEvent.dismissEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link DismissPushEvent} instance
     */
    @NonNull
    public static DismissPushEvent dismissEvent(
        @NonNull String pushId
    ) {
        return new DismissPushEvent(
            pushId
        );
    }

    /**
     * Creates an event representing expiration of a push notification.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * ExpiredPushEvent event = PushEvent.expiredEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link ExpiredPushEvent} instance
     */
    @NonNull
    public static ExpiredPushEvent expiredEvent(
        @NonNull String pushId
    ) {
        return new ExpiredPushEvent(
            pushId
        );
    }

    /**
     * Creates an event representing a push notification ignored by the application.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * IgnoredPushEvent event = PushEvent.ignoredEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @param category category of ignored event
     * @param details details of ignored event
     * @return a new {@link IgnoredPushEvent} instance
     */
    @NonNull
    public static IgnoredPushEvent ignoredEvent(
        @NonNull String pushId,
        @NonNull String category,
        @NonNull String details
    ) {
        return new IgnoredPushEvent(
            pushId
        ).withCategory(category)
            .withDetails(details);
    }

    /**
     * Creates an event representing the transmission of a response via a push notification.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * InlineAdditionalActionPushEvent event = PushEvent.inlineAdditionalActionEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @param actionId the ID of the action performed
     * @param text text that was written in inline action
     * @return a new {@link InlineAdditionalActionPushEvent} instance
     */
    @NonNull
    public static InlineAdditionalActionPushEvent inlineAdditionalActionEvent(
        @NonNull String pushId,
        @Nullable String actionId,
        @NonNull String text
    ) {
        return new InlineAdditionalActionPushEvent(
            pushId,
            text
        ).withActionId(actionId);
    }

    /**
     * Creates an event representing a push notification open.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * OpenPushEvent event = PushEvent.openEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link OpenPushEvent} instance
     */
    @NonNull
    public static OpenPushEvent openEvent(
        @NonNull String pushId
    ) {
        return new OpenPushEvent(
            pushId
        );
    }

    /**
     * Creates an event representing a push notification was explicitly removed by the application code.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * RemovedPushEvent event = PushEvent.removedEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link RemovedPushEvent} instance
     */
    @NonNull
    public static RemovedPushEvent removedEvent(
        @NonNull String pushId
    ) {
        return new RemovedPushEvent(
            pushId
        );
    }

    /**
     * Creates an event representing a silent push notification is processed.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * ProcessSilentPushEvent event = PushEvent.processSilentEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link ProcessSilentPushEvent} instance
     */
    @NonNull
    public static ProcessSilentPushEvent processSilentEvent(
        @NonNull String pushId
    ) {
        return new ProcessSilentPushEvent(
            pushId
        );
    }

    /**
     * Creates an event representing a push notification is received.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * ReceivePushEvent event = PushEvent.receiveEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link ReceivePushEvent} instance
     */
    @NonNull
    public static ReceivePushEvent receiveEvent(
        @NonNull String pushId
    ) {
        return new ReceivePushEvent(
            pushId
        );
    }

    /**
     * Creates an event representing a push notification is replaced by another push notification.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * ReplacePushEvent event = PushEvent.replaceEvent(pushId, newPushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @param newPushId the ID of the new push notification
     * @return a new {@link ReplacePushEvent} instance
     */
    @NonNull
    public static ReplacePushEvent replaceEvent(
        @NonNull String pushId,
        @Nullable String newPushId
    ) {
        return new ReplacePushEvent(
            pushId
        ).withNewPushId(newPushId);
    }

    /**
     * Creates an event representing a push notification is shown.
     *
     * <p><b>Sending event:</b>
     * <pre>{@code
     * // Create event
     * ShownPushEvent event = PushEvent.shownEvent(pushId)
     *     .withTransport(CoreConstants.Transport.FIREBASE);
     *
     * // Report event
     * AppMetricaPush.reportPushEvent(context, event);
     * }</pre>
     *
     * @param pushId the ID of the push notification
     * @return a new {@link ShownPushEvent} instance
     */
    @NonNull
    public static ShownPushEvent shownEvent(
        @NonNull String pushId
    ) {
        return new ShownPushEvent(
            pushId
        );
    }

    /**
     * Creates an event from intent.
     *
     * @param intent the intent with {@link NotificationActionInfo}
     * @return a new {@link PushEvent} instance
     */
    @Nullable
    public static PushEvent fromIntent(
        @NonNull Intent intent
    ) {
        return new IntentToPushEventConverter().convert(intent);
    }
}
