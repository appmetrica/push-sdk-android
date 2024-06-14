package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.ModuleEvent;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.utils.AppMetricaTrackerEventIdGenerator;
import io.appmetrica.analytics.push.settings.PushMessageTracker;
import java.util.Map;

public class BaseAppMetricaPushMessageTracker implements PushMessageTracker {

    private static final String PUSH_MESSAGE_SCOPE = "app";

    @NonNull
    private final AppMetricaTrackerEventIdGenerator appMetricaTrackerEventIdGenerator;

    public  BaseAppMetricaPushMessageTracker(@NonNull PreferenceManager preferenceManager) {
        appMetricaTrackerEventIdGenerator =
            new AppMetricaTrackerEventIdGenerator(preferenceManager, PUSH_MESSAGE_SCOPE);
    }

    @Override
    public void onPushTokenInited(@NonNull final String value, @NonNull String transport) {
        reportEvent(new AppMetricaPushTokenEvent(value, transport));
    }

    @Override
    public void onPushTokenUpdated(@NonNull final String value, @NonNull String transport) {
        reportEvent(new AppMetricaPushTokenEvent(value, transport));
    }

    @Override
    public void onMessageReceived(@NonNull final String pushId,
                                  @Nullable final String payload,
                                  @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithReceiveAction(pushId, transport));
    }

    @Override
    public void onNotificationCleared(@NonNull final String pushId,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithDismissAction(pushId, transport));
    }

    @Override
    public void onPushOpened(@NonNull final String pushId,
                             @Nullable final String payload,
                             @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithOpenAction(pushId, transport));
    }

    @Override
    public void onNotificationAdditionalAction(@NonNull final String pushId,
                                               @Nullable final String actionId,
                                               @Nullable final String payload,
                                               @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithAdditionalAction(pushId, actionId, transport));
    }

    @Override
    public void onNotificationInlineAdditionalAction(@NonNull final String pushId,
                                                     @Nullable final String actionId,
                                                     @Nullable final String payload,
                                                     @NonNull final String text,
                                                     @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithInlineAdditionalAction(pushId, actionId, text, transport));
    }

    @Override
    public void onSilentPushProcessed(@NonNull final String pushId,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithProcessedAction(pushId, transport));
    }

    @Override
    public void onNotificationShown(@NonNull String pushId,
                                    @Nullable final String payload,
                                    @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithShownAction(pushId, transport));
    }

    @Override
    public void onNotificationIgnored(@NonNull final String pushId,
                                      @Nullable final String category,
                                      @Nullable final String details,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithIgnoredAction(pushId, category, details, transport));
    }

    @Override
    public void onNotificationExpired(@NonNull final String pushId,
                                      @Nullable final String category,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithExpiredAction(pushId, category, transport));
    }

    @Override
    public void onRemovingSilentPushProcessed(@NonNull final String pushId,
                                              @Nullable final String category,
                                              @Nullable final String details,
                                              @Nullable final String payload,
                                              @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithRemovedAction(pushId, category, details, transport));
    }

    @Override
    public void onNotificationReplace(@NonNull String pushId, @Nullable String newPushId, @NonNull String transport) {
        reportEvent(AppMetricaPushActionEvent.createWithReplaceAction(pushId, newPushId, transport));
    }

    private void reportEvent(@NonNull final AppMetricaEvent event) {
        final int type = event.getEventType();
        final String name = event.getEventName();
        final String value = event.getEventValue();
        final Map<String, Object> environment = event.getEventEnvironment();
        environment.put(AppMetricaPushEvent.EVENT_ENVIRONMENT_EVENT_ID, appMetricaTrackerEventIdGenerator.generate());
        ModulesFacade.reportEvent(
            ModuleEvent.newBuilder(type)
                .withName(name)
                .withValue(value)
                .withEnvironment(environment)
                .build()
        );
        AppMetrica.sendEventsBuffer();
    }
}
