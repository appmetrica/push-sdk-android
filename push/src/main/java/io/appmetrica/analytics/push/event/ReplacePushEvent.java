package io.appmetrica.analytics.push.event;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

/**
 * Event representing that a push notification has been replaced.
 *
 * <p><b>Receiving events:</b>
 * <pre>{@code
 * AppMetricaPush.addPushEventListener(new PushEventListener() {
 *     {@literal @}Override
 *     public void onNotificationReplace(@NonNull ReplacePushEvent event) {
 *         // send event to another analytics sdk
 *     }
 * });
 * }</pre>
 *
 * <p><b>Sending your event:</b>
 * <pre>{@code
 * // Create event
 * ReplacePushEvent event = PushEvent.replaceEvent(pushId, newPushId)
 *     .withTransport(CoreConstants.Transport.FIREBASE);
 *
 * // Report event
 * AppMetricaPush.reportPushEvent(context, event);
 * }</pre>
 *
 * @see PushEvent#replaceEvent(String, String)
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 * @see AppMetricaPush#addPushEventListener(PushEventListener)
 */
public class ReplacePushEvent extends PushEvent {

    @NonNull
    private String transport = CoreConstants.Transport.UNKNOWN;
    @NonNull
    private final String pushId;
    @Nullable
    private String newPushId;

    ReplacePushEvent(
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
    public ReplacePushEvent withTransport(@NonNull String transport) {
        this.transport = transport;
        return this;
    }

    /**
     * Builder method to set unique identifier of the new push notification.
     *
     * @param newPushId unique identifier of the new push notification
     * @return current builder instance for chaining
     */
    @NonNull
    public ReplacePushEvent withNewPushId(@Nullable String newPushId) {
        this.newPushId = newPushId;
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
     * Returns unique identifier of the new push notification.
     * @return unique identifier of the new push notification
     */
    @Nullable
    public String getNewPushId() {
        return newPushId;
    }
}
