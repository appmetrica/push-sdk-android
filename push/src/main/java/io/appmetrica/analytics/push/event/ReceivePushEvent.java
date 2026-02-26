package io.appmetrica.analytics.push.event;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

/**
 * Represents an event indicating that a push notification has been received.
 *
 * <p><b>Receiving events:</b>
 * <pre>{@code
 * AppMetricaPush.addPushEventListener(new PushEventListener() {
 *     {@literal @}Override
 *     public void onPushReceived(@NonNull ReceivePushEvent event) {
 *         // send event to another analytics sdk
 *     }
 * });
 * }</pre>
 *
 * <p><b>Sending your event:</b>
 * <pre>{@code
 * // Create event
 * ReceivePushEvent event = PushEvent.receiveEvent(pushId)
 *     .withTransport(CoreConstants.Transport.FIREBASE);
 *
 * // Report event
 * AppMetricaPush.reportPushEvent(context, event);
 * }</pre>
 *
 * @see PushEvent#receiveEvent(String)
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 * @see AppMetricaPush#addPushEventListener(PushEventListener)
 */
public class ReceivePushEvent extends PushEvent {

    @NonNull
    private String transport = CoreConstants.Transport.UNKNOWN;
    @NonNull
    private final String pushId;
    @Nullable
    private String payload;

    ReceivePushEvent(
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
    public ReceivePushEvent withTransport(@NonNull String transport) {
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
    public ReceivePushEvent withPayload(@Nullable String payload) {
        this.payload = payload;
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
}
