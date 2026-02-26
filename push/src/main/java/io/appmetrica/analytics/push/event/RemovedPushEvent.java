package io.appmetrica.analytics.push.event;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

/**
 * Event representing that a push notification has been removed by code.
 *
 * <p><b>Receiving events:</b>
 * <pre>{@code
 * AppMetricaPush.addPushEventListener(new PushEventListener() {
 *     {@literal @}Override
 *     public void onNotificationRemoved(@NonNull RemovedPushEvent event) {
 *         // send event to another analytics sdk
 *     }
 * });
 * }</pre>
 *
 * <p><b>Sending your event:</b>
 * <pre>{@code
 * // Create event
 * RemovedPushEvent event = PushEvent.removedEvent(pushId)
 *     .withTransport(CoreConstants.Transport.FIREBASE);
 *
 * // Report event
 * AppMetricaPush.reportPushEvent(context, event);
 * }</pre>
 *
 * @see PushEvent#removedEvent(String)
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 * @see AppMetricaPush#addPushEventListener(PushEventListener)
 */
public class RemovedPushEvent extends PushEvent {

    @NonNull
    private String transport = CoreConstants.Transport.UNKNOWN;
    @NonNull
    private final String pushId;
    @Nullable
    private String payload;
    @Nullable
    private String category;
    @Nullable
    private String details;

    RemovedPushEvent(
        @NonNull String pushId
    ) {
        this.pushId = pushId;
    }

    /**
     * Builder method to set transport type.
     *
     * @param transport transport type. See possible values in {@link CoreConstants.Transport}
     * @return current builder instance for chaining
     */
    @NonNull
    public RemovedPushEvent withTransport(@NonNull String transport) {
        this.transport = transport;
        return this;
    }

    /**
     * Builder method to set additional payload data. Will not be sent to AppMetrica.
     *
     * @param payload custom data associated with push, can be null
     * @return current builder instance for chaining
     */
    @NonNull
    public RemovedPushEvent withPayload(@Nullable String payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Builder method to set the category for this push event.
     *
     * @param category reason why push notification is removed
     * @return current builder instance for chaining
     */
    @NonNull
    public RemovedPushEvent withCategory(@Nullable String category) {
        this.category = category;
        return this;
    }

    /**
     * Builder method to set the details for this push event.
     *
     * @param details details on which push notification is removed
     * @return current builder instance for chaining
     */
    @NonNull
    public RemovedPushEvent withDetails(@Nullable String details) {
        this.details = details;
        return this;
    }

    /**
     * Returns push transport identifier.
     * @return push transport identifier. See possible values in {@link CoreConstants.Transport}
     */
    @NonNull
    public String getTransport() {
        return transport;
    }

    /**
     * Returns unique identifier of the push notification.
     * @return unique identifier of the push notification
     */
    @NonNull
    public String getPushId() {
        return pushId;
    }

    /**
     * Returns custom payload data if set, null otherwise.
     * @return custom payload data if set, null otherwise
     */
    @Nullable
    public String getPayload() {
        return payload;
    }

    /**
     * Returns category of this event if set, null otherwise.
     * @return category of this event if set, null otherwise
     */
    @Nullable
    public String getCategory() {
        return category;
    }

    /**
     * Returns details of this event if set, null otherwise.
     * @return details of this event if set, null otherwise
     */
    @Nullable
    public String getDetails() {
        return details;
    }
}
