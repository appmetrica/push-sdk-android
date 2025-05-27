package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PushMessageTrackerHub implements InternalPushMessageTracker {

    private static final PushMessageTrackerHub INSTANCE = new PushMessageTrackerHub();

    @NonNull
    private final List<InternalPushMessageTracker> trackers = new CopyOnWriteArrayList<>();

    @NonNull
    public static PushMessageTrackerHub getInstance() {
        return INSTANCE;
    }

    public void registerTracker(@NonNull final InternalPushMessageTracker tracker) {
        trackers.add(tracker);
    }

    @Override
    public void onPushTokenInited(@NonNull final String value, @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onPushTokenInited(value, transport);
        }
    }

    @Override
    public void onPushTokenUpdated(@NonNull final String value, @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onPushTokenUpdated(value, transport);
        }
    }

    @Override
    public void onMessageReceived(@NonNull final String pushId,
                                  @Nullable final String payload,
                                  @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onMessageReceived(pushId, payload, transport);
        }
    }

    @Override
    public void onNotificationCleared(@NonNull final String pushId,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationCleared(pushId, payload, transport);
        }
    }

    @Override
    public void onPushOpened(@NonNull final String pushId,
                             @Nullable final String payload,
                             @NonNull String transport,
                             @Nullable String uri) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onPushOpened(pushId, payload, transport, uri);
        }
    }

    @Override
    public void onNotificationAdditionalAction(@NonNull final String pushId,
                                               @Nullable final String actionId,
                                               @Nullable final String payload,
                                               @NonNull String transport,
                                               @Nullable final String uri) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationAdditionalAction(pushId, actionId, payload, transport, uri);
        }
    }

    @Override
    public void onSilentPushProcessed(@NonNull final String pushId,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onSilentPushProcessed(pushId, payload, transport);
        }
    }

    @Override
    public void onNotificationInlineAdditionalAction(@NonNull final String pushId,
                                                     @Nullable final String actionId,
                                                     @Nullable final String payload,
                                                     @NonNull final String text,
                                                     @NonNull String transport,
                                                     @Nullable final String uri) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationInlineAdditionalAction(pushId, actionId, payload, text, transport, uri);
        }
    }

    @Override
    public void onNotificationShown(@NonNull final String pushId,
                                    @Nullable final String payload,
                                    @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationShown(pushId, payload, transport);
        }
    }

    @Override
    public void onNotificationIgnored(@NonNull final String pushId,
                                      @Nullable final String category,
                                      @Nullable final String details,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationIgnored(pushId, category, details, payload, transport);
        }
    }

    @Override
    public void onNotificationExpired(@NonNull final String pushId,
                                      @Nullable final String category,
                                      @Nullable final String payload,
                                      @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationExpired(pushId, category, payload, transport);
        }
    }

    @Override
    public void onRemovingSilentPushProcessed(@NonNull final String pushId,
                                              @Nullable final String category,
                                              @Nullable final String details,
                                              @Nullable final String payload,
                                              @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onRemovingSilentPushProcessed(pushId, category, details, payload, transport);
        }
    }

    @Override
    public void onNotificationReplace(@NonNull String pushId, @Nullable String newPushId, @NonNull String transport) {
        for (final InternalPushMessageTracker tracker : trackers) {
            tracker.onNotificationReplace(pushId, newPushId, transport);
        }
    }
}
