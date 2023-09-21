package io.appmetrica.analytics.push.coreutils.internal.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackersHub implements Tracker {

    private static final TrackersHub INSTANCE = new TrackersHub();

    @NonNull
    private final List<Tracker> trackers = new CopyOnWriteArrayList<Tracker>();

    @NonNull
    public static TrackersHub getInstance() {
        return INSTANCE;
    }

    public void registerTracker(@NonNull final Tracker tracker) {
        trackers.add(tracker);
    }

    @Override
    public void resumeSession() {
        for (final Tracker tracker : trackers) {
            tracker.resumeSession();
        }
    }

    @Override
    public void pauseSession() {
        for (final Tracker tracker : trackers) {
            tracker.pauseSession();
        }
    }

    @Override
    public void reportEvent(@NonNull final String name) {
        for (final Tracker tracker : trackers) {
            tracker.reportEvent(name);
        }
    }

    @Override
    public void reportEvent(@NonNull final String name, @Nullable final Map<String, Object> attributes) {
        for (final Tracker tracker : trackers) {
            tracker.reportEvent(name, attributes);
        }
    }

    @Override
    public void reportError(@NonNull final String message, @Nullable final Throwable error) {
        for (final Tracker tracker : trackers) {
            tracker.reportError(message, error);
        }
    }

    @Override
    public void reportUnhandledException(@NonNull final Throwable exception) {
        for (final Tracker tracker : trackers) {
            tracker.reportUnhandledException(exception);
        }
    }
}
