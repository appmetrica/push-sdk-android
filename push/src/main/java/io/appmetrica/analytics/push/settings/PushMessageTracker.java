package io.appmetrica.analytics.push.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;

/**
 * Interface for custom {@link PushMessageTracker}.
 * Tracker can be set via {@link AppMetricaPush#addPushMessageTracker(PushMessageTracker)} method.
 */
public interface PushMessageTracker {

    /**
     * Called when push token occurred for the first time.
     *
     * @param value token value
     * @param transport transport service title
     */
    void onPushTokenInited(@NonNull final String value,
                           @NonNull String transport);

    /**
     * Called when push token updated.
     *
     * @param value token value
     * @param transport transport service title
     */
    void onPushTokenUpdated(@NonNull final String value,
                            @NonNull String transport);

    /**
     * Called when push message received.
     *
     * @param pushId push ID
     * @param payload push payload
     * @param transport transport service title
     */
    void onMessageReceived(@NonNull final String pushId,
                           @Nullable final String payload,
                           @NonNull String transport);

    /**
     * Called when notification cleared.
     *
     * @param pushId push ID
     * @param payload push payload
     * @param transport transport service title
     */
    void onNotificationCleared(@NonNull final String pushId,
                               @Nullable final String payload,
                               @NonNull String transport);

    /**
     * Called when notification opened.
     *
     * @param pushId push ID
     * @param payload push payload
     * @param transport transport service title
     */
    void onPushOpened(@NonNull final String pushId,
                      @Nullable final String payload,
                      @NonNull String transport);

    /**
     * Called when notification additional action called.
     *
     * @param pushId push ID
     * @param actionId action ID
     * @param payload push payload
     * @param transport transport service title
     */
    void onNotificationAdditionalAction(@NonNull final String pushId,
                                        @Nullable final String actionId,
                                        @Nullable final String payload,
                                        @NonNull String transport);

    /**
     * Called when silent push processed.
     *
     * @param pushId push ID
     * @param payload push payload
     * @param transport transport service title
     */
    void onSilentPushProcessed(@NonNull final String pushId,
                               @Nullable final String payload,
                               @NonNull String transport);

    /**
     * Called when notification inline additional action called.
     *
     * @param pushId push ID
     * @param actionId action ID
     * @param payload push payload
     * @param text inline action text
     * @param transport transport service title
     */
    void onNotificationInlineAdditionalAction(@NonNull final String pushId,
                                              @Nullable final String actionId,
                                              @Nullable final String payload,
                                              @NonNull final String text,
                                              @NonNull String transport);

    /**
     * Called when notification shown.
     *
     * @param pushId push ID
     * @param payload push payload
     * @param transport transport service title
     */
    void onNotificationShown(@NonNull final String pushId,
                             @Nullable final String payload,
                             @NonNull String transport);

    /**
     * Called when notification ignored.
     *
     * @param pushId push ID
     * @param category reason category
     * @param details reason details
     * @param payload push payload
     * @param transport transport service title
     */
    void onNotificationIgnored(@NonNull final String pushId,
                               @Nullable final String category,
                               @Nullable final String details,
                               @Nullable final String payload,
                               @NonNull String transport);

    /**
     * Called when notification expired.
     *
     * @param pushId push ID
     * @param category reason category
     * @param payload push payload
     * @param transport transport service title
     */
    void onNotificationExpired(@NonNull final String pushId,
                               @Nullable final String category,
                               @Nullable final String payload,
                               @NonNull String transport);

    /**
     * Called when removing silent push processed.
     *
     * @param pushId push ID
     * @param category reason category
     * @param details reason details
     * @param payload push payload
     * @param transport transport service title
     */
    void onRemovingSilentPushProcessed(@NonNull final String pushId,
                                       @Nullable final String category,
                                       @Nullable final String details,
                                       @Nullable final String payload,
                                       @NonNull String transport);

    /**
     * Called when notification replaced.
     *
     * @param pushId push ID
     * @param newPushId new push ID
     * @param transport transport service title
     */
    void onNotificationReplace(@NonNull final String pushId,
                               @Nullable final String newPushId,
                               @NonNull String transport);
}
