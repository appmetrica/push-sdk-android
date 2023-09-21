package io.appmetrica.analytics.push.coreutils.internal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Internal class. Can be changed.
 */
public class RefreshTokenInfo {

    private static final String FORCE_KEY = "FORCE";
    private static final String NOTIFICATION_STATUS_CHANGED_TIME_KEY = "NOTIFICATION_STATUS_CHANGED_TIME_KEY";

    public final boolean force;
    @Nullable
    public final Long notificationStatusChangedTime;

    public RefreshTokenInfo(boolean force) {
        this(force, null);
    }

    public RefreshTokenInfo(boolean force, @Nullable Long notificationStatusChangedTime) {
        this.force = force;
        this.notificationStatusChangedTime = notificationStatusChangedTime;
    }

    @NonNull
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(FORCE_KEY, force);
        if (notificationStatusChangedTime != null) {
            bundle.putLong(NOTIFICATION_STATUS_CHANGED_TIME_KEY, notificationStatusChangedTime);
        }
        return bundle;
    }

    @NonNull
    public static RefreshTokenInfo fromBundle(@Nullable Bundle bundle) {
        if (bundle == null) {
            return new RefreshTokenInfo(false);
        }
        Long notificationStatusChangedTime = null;
        if (bundle.containsKey(NOTIFICATION_STATUS_CHANGED_TIME_KEY)) {
            notificationStatusChangedTime = bundle.getLong(NOTIFICATION_STATUS_CHANGED_TIME_KEY);
        }
        return new RefreshTokenInfo(
            bundle.getBoolean(FORCE_KEY, false),
            notificationStatusChangedTime
        );
    }
}
