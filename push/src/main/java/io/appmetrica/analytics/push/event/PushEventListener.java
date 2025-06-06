package io.appmetrica.analytics.push.event;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;

/**
 * Push events listener with default implementation.
 */
public abstract class PushEventListener {

    /**
     * Called when a push message is received.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onPushReceived(@NonNull final ReceivePushEvent event) {}

    /**
     * Called when notification dismissed/cleared.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationDismissed(@NonNull final DismissPushEvent event) {}

    /**
     * Called when notification opened.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationOpened(@NonNull final OpenPushEvent event) {}

    /**
     * Called when notification additional action called.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationAdditionalAction(@NonNull final AdditionalActionPushEvent event) {}

    /**
     * Called when silent push processed.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onSilentPushProcessed(@NonNull final ProcessSilentPushEvent event) {}

    /**
     * Called when notification inline additional action called.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationInlineAdditionalAction(@NonNull final InlineAdditionalActionPushEvent event) {}

    /**
     * Called when notification shown.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationShown(@NonNull final ShownPushEvent event) {}

    /**
     * Called when notification ignored.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationIgnored(@NonNull final IgnoredPushEvent event) {}

    /**
     * Called when notification expired.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationExpired(@NonNull final ExpiredPushEvent event) {}

    /**
     * Called when notification is removed by code.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationRemoved(@NonNull final RemovedPushEvent event) {}

    /**
     * Called when notification replaced.
     *
     * @param event the received push event containing details about the push message
     * @see AppMetricaPush#addPushEventListener(PushEventListener)
     */
    public void onNotificationReplace(@NonNull final ReplacePushEvent event) {}
}
