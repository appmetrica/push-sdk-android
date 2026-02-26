package io.appmetrica.analytics.push.event;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

/**
 * Event representing a user action on push notification button.
 *
 * <p><b>Receiving events:</b>
 * <pre>{@code
 * AppMetricaPush.addPushEventListener(new PushEventListener() {
 *     {@literal @}Override
 *     public void onNotificationAdditionalAction(@NonNull AdditionalActionPushEvent event) {
 *         // send event to another analytics sdk
 *     }
 * });
 * }</pre>
 *
 * <p><b>Sending your event:</b>
 * <pre>{@code
 * // Create event
 * AdditionalActionPushEvent event = PushEvent.additionalActionEvent(pushId, actionId)
 *     .withTransport(CoreConstants.Transport.FIREBASE);
 *
 * // Report event
 * AppMetricaPush.reportPushEvent(context, event);
 * }</pre>
 *
 * @see PushEvent#additionalActionEvent(String, String)
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 * @see AppMetricaPush#addPushEventListener(PushEventListener)
 */
public class AdditionalActionPushEvent extends PushEvent {

    @NonNull
    private String transport = CoreConstants.Transport.UNKNOWN;
    @NonNull
    private final String pushId;
    @Nullable
    private String actionId;
    @Nullable
    private String targetActionUri;
    @Nullable
    private String payload;

    AdditionalActionPushEvent(
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
    public AdditionalActionPushEvent withTransport(@NonNull String transport) {
        this.transport = transport;
        return this;
    }

    /**
     * Builder method to set the button identifier.
     *
     * @param actionId unique identifier for the button action, can be null
     * @return current builder instance for chaining
     */
    @NonNull
    public AdditionalActionPushEvent withActionId(@Nullable String actionId) {
        this.actionId = actionId;
        return this;
    }

    /**
     * Builder method to set the URI that was opened when button is clicked.
     *
     * @param targetActionUri URI that was opened, can be null
     * @return current builder instance for chaining
     */
    @NonNull
    public AdditionalActionPushEvent withTargetActionUri(@Nullable String targetActionUri) {
        this.targetActionUri = targetActionUri;
        return this;
    }

    /**
     * Builder method to set additional payload data. Will not be sent to AppMetrica.
     *
     * @param payload custom data associated with push, can be null
     * @return current builder instance for chaining
     */
    @NonNull
    public AdditionalActionPushEvent withPayload(@Nullable String payload) {
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
     * Returns button action identifier if set, null otherwise.
     * @return button action identifier if set, null otherwise
     */
    @Nullable
    public String getActionId() {
        return actionId;
    }

    /**
     * Returns URI that was opened by this button if set, null otherwise.
     * @return URI that was opened by this button if set, null otherwise
     */
    @Nullable
    public String getTargetActionUri() {
        return targetActionUri;
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
