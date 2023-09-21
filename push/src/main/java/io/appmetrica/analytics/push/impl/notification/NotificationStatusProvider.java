package io.appmetrica.analytics.push.impl.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.NotificationManagerCompat;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.utils.Utils;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationStatusProvider {

    @Nullable
    private final NotificationManager notificationManager;
    @NonNull
    private final NotificationManagerCompat notificationManagerCompat;
    @NonNull
    private final NotificationStatusProviderImpl impl;

    @NonNull
    private final PreferenceManager preferenceManager;

    public NotificationStatusProvider(@NonNull final Context context) {
        this(
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE),
            NotificationManagerCompat.from(context),
            new PreferenceManager(context, PreferenceManager.NOTIFICATION_STATUS_PREFERENCES_SUFFIX)
        );
    }

    @NonNull
    public NotificationStatus getNotificationStatus() {
        return impl.getNotificationStatus();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private class NotificationStatusProviderApi28Impl extends NotificationStatusProviderApi26Impl {

        protected NotificationStatusProviderApi28Impl() {
            super();
        }

        @SuppressLint("NewApi")
        @Override
        protected boolean areNotificationChannelGroupEnabled(@NonNull final NotificationChannelGroup group) {
            return !group.isBlocked();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private class NotificationStatusProviderApi26Impl extends NotificationStatusProviderBaseImpl {

        protected NotificationStatusProviderApi26Impl() {
            super();
        }

        @NonNull
        @Override
        public NotificationStatus getNotificationStatus() {
            List<NotificationChannel> channels = getChannels();
            List<NotificationChannelGroup> groups = getChannelGroups();

            Map<String, Set<NotificationStatus.Channel>> channelMap =
                new HashMap<String, Set<NotificationStatus.Channel>>();
            Set<NotificationStatus.Channel> channelWithoutGroupSet =
                new HashSet<NotificationStatus.Channel>();
            for (NotificationChannel channel : channels) {
                boolean enabled = areNotificationChannelEnabled(channel);
                boolean changed = updateNotificationChannelEnabled(channel.getId(), enabled);
                if (channel.getGroup() == null) {
                    channelWithoutGroupSet.add(new NotificationStatus.Channel(channel.getId(), enabled, changed));
                } else {
                    Set<NotificationStatus.Channel> channelSet = channelMap.get(channel.getGroup());
                    if (channelSet == null) {
                        channelSet = new HashSet<NotificationStatus.Channel>();
                        channelMap.put(channel.getGroup(), channelSet);
                    }
                    channelSet.add(new NotificationStatus.Channel(channel.getId(), enabled, changed));
                }
            }

            Set<NotificationStatus.Group> groupSet = new HashSet<NotificationStatus.Group>();
            for (NotificationChannelGroup group : groups) {
                boolean enabled = areNotificationChannelGroupEnabled(group);
                boolean changed = updateNotificationChannelGroupEnabled(group.getId(), enabled);
                groupSet.add(
                    new NotificationStatus.Group(
                        group.getId(),
                        channelMap.get(group.getId()),
                        enabled,
                        changed
                    )
                );
            }

            boolean enabled = areNotificationsEnabled();
            boolean changed = updateAppNotificationEnabled(enabled);
            return new NotificationStatus(groupSet, channelWithoutGroupSet, enabled, changed);
        }

        @NonNull
        protected List<NotificationChannelGroup> getChannelGroups() {
            if (notificationManager != null) {
                try {
                    return notificationManager.getNotificationChannelGroups();
                } catch (Exception e) {
                    PublicLogger.e(e, e.getMessage());
                }
            }
            return Collections.emptyList();
        }

        protected boolean areNotificationChannelGroupEnabled(@NonNull final NotificationChannelGroup group) {
            return true;
        }

        protected boolean updateNotificationChannelGroupEnabled(@NonNull final String groupId, boolean enabled) {
            Boolean oldValue = preferenceManager.getNotificationChannelGroupStatus(groupId);
            preferenceManager.saveNotificationChannelGroupStatus(groupId, enabled);
            return oldValue != null && oldValue != enabled;
        }

        @NonNull
        protected List<NotificationChannel> getChannels() {
            if (notificationManager != null) {
                try {
                    return notificationManager.getNotificationChannels();
                } catch (Exception e) {
                    PublicLogger.e(e, e.getMessage());
                }
            }
            return Collections.emptyList();
        }

        protected boolean areNotificationChannelEnabled(@NonNull final NotificationChannel channel) {
            return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        }

        protected boolean updateNotificationChannelEnabled(@NonNull String channelId, boolean enabled) {
            Boolean oldValue = preferenceManager.getNotificationChannelStatus(channelId);
            preferenceManager.saveNotificationChannelStatus(channelId, enabled);
            return oldValue != null && oldValue != enabled;
        }
    }

    private class NotificationStatusProviderBaseImpl implements NotificationStatusProviderImpl {

        protected NotificationStatusProviderBaseImpl() {}

        @NonNull
        @Override
        public NotificationStatus getNotificationStatus() {
            boolean enabled = areNotificationsEnabled();
            boolean changed = updateAppNotificationEnabled(enabled);
            return new NotificationStatus(enabled, changed);
        }

        protected boolean areNotificationsEnabled() {
            return notificationManagerCompat.areNotificationsEnabled();
        }

        protected boolean updateAppNotificationEnabled(boolean enabled) {
            Boolean oldValue = preferenceManager.getAppNotificationStatus();
            preferenceManager.saveAppNotificationStatus(enabled);
            return oldValue != null && oldValue != enabled;
        }
    }

    private interface NotificationStatusProviderImpl {

        @NonNull
        NotificationStatus getNotificationStatus();

    }

    @VisibleForTesting
    NotificationStatusProvider(@Nullable final NotificationManager notificationManager,
                               @NonNull final NotificationManagerCompat notificationManagerCompat,
                               @NonNull final PreferenceManager preferenceManager) {
        this.notificationManager = notificationManager;
        this.notificationManagerCompat = notificationManagerCompat;
        this.preferenceManager = preferenceManager;
        if (Utils.isApiAchived(Build.VERSION_CODES.P)) {
            impl = new NotificationStatusProviderApi28Impl();
        } else if (Utils.isApiAchived(Build.VERSION_CODES.O)) {
            impl = new NotificationStatusProviderApi26Impl();
        } else {
            impl = new NotificationStatusProviderBaseImpl();
        }
    }
}
