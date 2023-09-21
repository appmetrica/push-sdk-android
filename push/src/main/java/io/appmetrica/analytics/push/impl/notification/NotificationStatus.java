package io.appmetrica.analytics.push.impl.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class NotificationStatus {

    public static class Group {

        @NonNull
        public final String id;
        @NonNull
        public final Set<Channel> channels;
        public final boolean enabled;
        public final boolean changed;

        public Group(@NonNull String id, @Nullable Set<Channel> channels, boolean enabled, boolean changed) {
            this.id = id;
            this.enabled = enabled;
            this.channels = channels == null ? Collections.<Channel>emptySet() : Collections.unmodifiableSet(channels);
            this.changed = changed;
        }
    }

    public static class Channel {

        @NonNull
        public final String id;
        public final boolean enabled;
        public final boolean changed;

        public Channel(@NonNull String id, boolean enabled, boolean changed) {
            this.id = id;
            this.enabled = enabled;
            this.changed = changed;
        }
    }

    @NonNull
    public final Set<Group> groups;
    @NonNull
    public final Set<Channel> channelsWithoutGroup;
    public final boolean enabled;
    public final boolean changed;
    @Nullable
    private Long changedTime;

    public NotificationStatus(final boolean enabled, final boolean changed) {
        this(Collections.<Group>emptySet(), Collections.<Channel>emptySet(), enabled, changed);
    }

    public NotificationStatus(@NonNull final Set<Group> groups,
                              @NonNull final Set<Channel> channelsWithoutGroup,
                              final boolean enabled,
                              final boolean changed) {
        this.groups = Collections.unmodifiableSet(groups);
        this.channelsWithoutGroup = Collections.unmodifiableSet(channelsWithoutGroup);
        this.enabled = enabled;
        this.changed = changed;
    }

    @Nullable
    public Long getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(@Nullable Long changedTime) {
        this.changedTime = changedTime;
    }
}
