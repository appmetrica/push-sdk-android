package io.appmetrica.analytics.push.event;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

/**
 * Event that represents the transmission of a response via a push notification.
 *
 * <p><b>Receiving events:</b>
 * <pre>{@code
 * AppMetricaPush.addPushEventListener(new PushEventListener() {
 *     {@literal @}Override
 *     public void onNotificationInlineAdditionalAction(@NonNull InlineAdditionalActionPushEvent event) {
 *         // send event to another analytics sdk
 *     }
 * });
 * }</pre>
 *
 * <p><b>Sending your event:</b>
 * <pre>{@code
 * // Create event
 * InlineAdditionalActionPushEvent event = PushEvent.inlineAdditionalActionEvent(pushId, actionId, text)
 *     .withTransport(CoreConstants.Transport.FIREBASE);
 *
 * // Report event
 * AppMetricaPush.reportPushEvent(context, event);
 * }</pre>
 *
 * @see PushEvent#inlineAdditionalActionEvent(String, String, String)
 * @see AppMetricaPush#reportPushEvent(Context, PushEvent)
 * @see AppMetricaPush#addPushEventListener(PushEventListener)
 */
public class InlineAdditionalActionPushEvent extends PushEvent {

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
    @NonNull
    private final String text;

    InlineAdditionalActionPushEvent(
        @NonNull String pushId,
        @NonNull String text
    ) {
        this.pushId = pushId;
        this.text = text;
    }

    /**
     * Builder method to set transport type.
     *
     * @param transport transport type. See possible values in {@link CoreConstants.Transport}
     * @return current builder instance for chaining
     */
    @NonNull
    public InlineAdditionalActionPushEvent withTransport(@NonNull String transport) {
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
    public InlineAdditionalActionPushEvent withActionId(@Nullable String actionId) {
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
    public InlineAdditionalActionPushEvent withTargetActionUri(@Nullable String targetActionUri) {
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
    public InlineAdditionalActionPushEvent withPayload(@Nullable String payload) {
        this.payload = payload;
        return this;
    }

    /**
     * @return push transport identifier. See possible values in {@link CoreConstants.Transport}
     */
    @NonNull
    public String getTransport() {
        return transport;
    }

    /**
     * @return unique identifier of the push notification
     */
    @NonNull
    public String getPushId() {
        return pushId;
    }

    /**
     * @return button action identifier if set, null otherwise
     */
    @Nullable
    public String getActionId() {
        return actionId;
    }

    /**
     * @return URI that was opened by this button if set, null otherwise
     */
    @Nullable
    public String getTargetActionUri() {
        return targetActionUri;
    }

    /**
     * @return custom payload data if set, null otherwise
     */
    @Nullable
    public String getPayload() {
        return payload;
    }

    /**
     * @return the text associated with this inline additional action
     */
    @NonNull
    public String getText() {
        return text;
    }
}
