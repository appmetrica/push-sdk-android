package io.appmetrica.analytics.push.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;

public class PreferenceManager {

    private static final String TAG = "[PreferenceManager]";

    private static final int VERSION = 1;

    private interface MigrationScript {

        void migrate(@NonNull SharedPreferences preferences);

    }

    private static final String PUSH_PREFERENCES_SUFFIX = ".STORAGE";
    public static final String NOTIFICATION_STATUS_PREFERENCES_SUFFIX = ".NOTIFICATION_STATUS";

    private static final String PREF_KEY_STORAGE_VERSION = "storage_version";

    private static final String PREF_KEY_PENDING_INTENT_ID = "pending_intent_id";
    private static final String PREF_KEY_RELATED_PUSH_NOTIFICATION_IDS = "refated_push_notification_ids";
    private static final String PREF_KEY_RELATED_CONTENT_NOTIFICATION_IDS = "refated_content_notification_ids";
    private static final String PREF_KEY_RELATED_PUSH_NOTIFICATION_INFO_LIST = "refated_push_notification_info_list";
    private static final String PREF_PREFIX_SHOWN_TIMES_MILLIS_BY_CHANNEL_ID = "shown_times_millis_by_channel_id";

    private static final String PREF_KEY_APP_NOTIFICATION_STATUS = "app_notification_status";
    private static final String PREF_PREFIX_NOTIFICATION_CHANNEL_STATUS = "notification_channel_";
    private static final String PREF_PREFIX_NOTIFICATION_CHANNEL_GROUP_STATUS = "notification_group_";

    private static final String MANAGER_LAST_TOKENS = "io.appmetrica.analytics.push.all_tokens";
    @Deprecated
    private static final String MANAGER_LAST_TOKEN = "io.appmetrica.analytics.push.token";
    @Deprecated
    private static final String MANAGER_LAST_TOKEN_UPDATE_TIME = "io.appmetrica.analytics.push.token.last.update.time";
    private static final String PREF_KEY_APPMETRICA_TRACKER_EVENT_ID = "appmetrica_tracker_event_id_";

    @NonNull
    private final Context context;
    @NonNull
    private final String prefsName;

    private final SparseArray<MigrationScript> migrationScripts = new SparseArray<MigrationScript>();

    {
        migrationScripts.put(1, new MigrationScript() {
            @Override
            public void migrate(@NonNull SharedPreferences preferences) {
                preferences.edit().remove(MANAGER_LAST_TOKEN).remove(MANAGER_LAST_TOKEN_UPDATE_TIME).apply();
            }
        });
    }

    public PreferenceManager(@NonNull final Context context) {
        this(context, PUSH_PREFERENCES_SUFFIX);
    }

    public PreferenceManager(@NonNull final Context context, @NonNull final String suffix) {
        this.context = context;
        prefsName = this.context.getPackageName() + suffix;
        migrate();
    }

    private void migrate() {
        int actualVersion = getPreferences().getInt(PREF_KEY_STORAGE_VERSION, 0);
        DebugLogger.INSTANCE.info(TAG, "actual version %d. Required version %d", actualVersion, VERSION);
        if (actualVersion < VERSION) {
            DebugLogger.INSTANCE.info(TAG, "will run migration scripts");
            for (int i = actualVersion; i <= VERSION; i++) {
                MigrationScript script = migrationScripts.get(i);
                if (script != null) {
                    DebugLogger.INSTANCE.info(TAG, "run script for version %d", i);
                    script.migrate(getPreferences());
                }
            }
            saveInt(PREF_KEY_STORAGE_VERSION, VERSION);
        }
    }

    @NonNull
    public SharedPreferences getPreferences() {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    @NonNull
    public PreferenceManager savePendingIntentId(final int value) {
        return saveInt(PREF_KEY_PENDING_INTENT_ID, value);
    }

    public int getPendingIntentId(final int defValue) {
        return getInt(PREF_KEY_PENDING_INTENT_ID, defValue);
    }

    @NonNull
    public PreferenceManager savePushIds(@Nullable final String relatedIds) {
        return saveString(PREF_KEY_RELATED_PUSH_NOTIFICATION_IDS, relatedIds);
    }

    @Nullable
    public String getPushIds(@Nullable final String defValue) {
        return getString(PREF_KEY_RELATED_PUSH_NOTIFICATION_IDS, defValue);
    }

    @NonNull
    public PreferenceManager saveContentIds(@Nullable final String relatedIds) {
        return saveString(PREF_KEY_RELATED_CONTENT_NOTIFICATION_IDS, relatedIds);
    }

    @Nullable
    public String getContentIds(@Nullable final String defValue) {
        return getString(PREF_KEY_RELATED_CONTENT_NOTIFICATION_IDS, defValue);
    }

    @NonNull
    public PreferenceManager savePushInfoList(@Nullable final String info) {
        return saveString(PREF_KEY_RELATED_PUSH_NOTIFICATION_INFO_LIST, info);
    }

    @Nullable
    public String getPushInfoList(@Nullable final String defValue) {
        return getString(PREF_KEY_RELATED_PUSH_NOTIFICATION_INFO_LIST, defValue);
    }

    @NonNull
    public PreferenceManager saveShownTimesForChannelId(@Nullable String channelId, @NonNull String times) {
        return saveString(PREF_PREFIX_SHOWN_TIMES_MILLIS_BY_CHANNEL_ID + channelId, times);
    }

    @Nullable
    public String getShownTimesForChannelId(@Nullable final String channelId, @Nullable String defValue) {
        return getString(PREF_PREFIX_SHOWN_TIMES_MILLIS_BY_CHANNEL_ID + channelId, defValue);
    }

    @NonNull
    public PreferenceManager saveString(@NonNull final String key, @Nullable final String value) {
        getPreferences().edit().putString(key, value).apply();
        return this;
    }

    @Nullable
    public String getString(@NonNull final String key, @Nullable final String defValue) {
        return getPreferences().getString(key, defValue);
    }

    @NonNull
    public PreferenceManager saveInt(@NonNull final String key, final int value) {
        getPreferences().edit().putInt(key, value).apply();
        return this;
    }

    @NonNull
    public Integer getInt(@NonNull final String key, final int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    @NonNull
    public PreferenceManager saveLong(@NonNull final String key, final long value) {
        getPreferences().edit().putLong(key, value).apply();
        return this;
    }

    @NonNull
    public Long getLong(@NonNull final String key, final long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    @NonNull
    public PreferenceManager saveBoolean(@NonNull final String key, final boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
        return this;
    }

    @Nullable
    public Boolean getBoolean(@NonNull final String key) {
        return getPreferences().contains(key) ? getPreferences().getBoolean(key, false) : null;
    }

    @NonNull
    public PreferenceManager saveAppNotificationStatus(final boolean value) {
        return saveBoolean(PREF_KEY_APP_NOTIFICATION_STATUS, value);
    }

    @Nullable
    public Boolean getAppNotificationStatus() {
        return getBoolean(PREF_KEY_APP_NOTIFICATION_STATUS);
    }

    @NonNull
    public PreferenceManager saveNotificationChannelStatus(@NonNull final String channelId, final boolean value) {
        return saveBoolean(PREF_PREFIX_NOTIFICATION_CHANNEL_STATUS + channelId, value);
    }

    @Nullable
    public Boolean getNotificationChannelStatus(@NonNull final String channelId) {
        return getBoolean(PREF_PREFIX_NOTIFICATION_CHANNEL_STATUS + channelId);
    }

    @NonNull
    public PreferenceManager saveNotificationChannelGroupStatus(@NonNull final String groupId, final boolean value) {
        return saveBoolean(PREF_PREFIX_NOTIFICATION_CHANNEL_GROUP_STATUS + groupId, value);
    }

    @Nullable
    public Boolean getNotificationChannelGroupStatus(@NonNull final String groupId) {
        return getBoolean(PREF_PREFIX_NOTIFICATION_CHANNEL_GROUP_STATUS + groupId);
    }

    @NonNull
    public PreferenceManager saveTokens(@Nullable String value) {
        return saveString(MANAGER_LAST_TOKENS, value);
    }

    @Nullable
    public String getTokens() {
        return getString(MANAGER_LAST_TOKENS, null);
    }

    @NonNull
    public PreferenceManager saveAppMetricaTrackerEventId(@NonNull String scope, long id) {
        return saveLong(PREF_KEY_APPMETRICA_TRACKER_EVENT_ID + scope, id);
    }

    @NonNull
    public Long getAppMetricaTrackerEventId(@NonNull String scope, long defValue) {
        return getLong(PREF_KEY_APPMETRICA_TRACKER_EVENT_ID + scope, defValue);
    }

    @VisibleForTesting
    public void clearAll() {
        getPreferences().edit().clear().apply();
    }
}
