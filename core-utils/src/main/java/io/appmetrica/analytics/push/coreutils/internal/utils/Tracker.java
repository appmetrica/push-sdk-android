package io.appmetrica.analytics.push.coreutils.internal.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;

public interface Tracker {

    public void resumeSession();

    public void pauseSession();

    public void reportEvent(@NonNull final String name);

    public void reportEvent(@NonNull final String name, @Nullable final Map<String, Object> attributes);

    public void reportError(@NonNull final String message, @Nullable final Throwable error);

    public void reportUnhandledException(@NonNull final Throwable exception);

}
