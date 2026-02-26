package io.appmetrica.analytics.push.event;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

/**
 * Event representing that a push notification has been expired.
 *
 * <p><b>Receiving events:</b>
 * <pre>{@code
 * AppMetricaPush.addPushEventListener(new PushEventListener() {
 *     {@literal @}Override
 *     public void onNotificationExpired(@NonNull ExpiredPushEvent event) {
 *         // send event to another analytics sdk
 *     }
 * });
 * }</pre>
 *
 * <p><b>Sending your event:</b>
 * <pre>{@code
 * // Create event
 * ExpiredPushEvent event = PushEvent.expiredEvent(pushId)
 *     .withTransport(CoreConstants.Transport.FIREBASE);
 *
 * // Report event
 * AppMetricaPush.reportPushEvent(context, event);
 * }</pre>
 *
 * @see PushEvent#expiredEvent(String)
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 * @see AppMetricaPush#addPushEventListener(PushEventListener)
 */
public class ExpiredPushEvent extends PushEvent {

    @NonNull
    private String transport = CoreConstants.Transport.UNKNOWN;
    @NonNull
    private final String pushId;
    @Nullable
    private String payload;
    @Nullable
    private String category;

    ExpiredPushEvent(
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
    public ExpiredPushEvent withTransport(@NonNull String transport) {
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
    public ExpiredPushEvent withPayload(@Nullable String payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Builder method to set the category for this push event.
     *
     * @param category reason why push notification is expired
     * @return current builder instance for chaining
     */
    @NonNull
    public ExpiredPushEvent withCategory(@Nullable String category) {
        this.category = category;
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
}
