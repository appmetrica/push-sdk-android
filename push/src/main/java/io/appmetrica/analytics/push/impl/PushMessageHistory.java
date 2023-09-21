package io.appmetrica.analytics.push.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushMessageHistory {

    @VisibleForTesting
    static final int HISTORY_RECORDS_LIMIT = 50;

    private class JsonKeys {
        private static final String PUSH_ID = "push_id";
        private static final String CONTENT_ID = "content_id";
        private static final String NOTIFICATION_ID = "notification_id";
        private static final String NOTIFICATION_TAG = "notification_tag";
        private static final String IS_ACTIVE = "active";
    }

    public static class PushInfo {

        @NonNull
        public final String pushId;
        @NonNull
        public final Integer notificationId;
        @Nullable
        public final String notificationTag;
        @Nullable
        public final Boolean isActive;

        public PushInfo(@NonNull final String pushId,
                        @NonNull final Integer notificationId,
                        @Nullable final String notificationTag,
                        @Nullable final Boolean isActive) {
            this.pushId = pushId;
            this.notificationId = notificationId;
            this.notificationTag = notificationTag;
            this.isActive = isActive;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PushInfo pushInfo = (PushInfo) o;

            if (!notificationId.equals(pushInfo.notificationId)) return false;
            if (notificationTag != null) {
                return notificationTag.equals(pushInfo.notificationTag);
            } else {
                return pushInfo.notificationTag == null;
            }
        }

        @Override
        public int hashCode() {
            int result = notificationId.hashCode();
            result = 31 * result + (notificationTag != null ? notificationTag.hashCode() : 0);
            return result;
        }

        @NonNull
        private JSONObject toJson() throws JSONException {
            return new JSONObject()
                .put(JsonKeys.PUSH_ID, pushId)
                .put(JsonKeys.NOTIFICATION_ID, notificationId)
                .put(JsonKeys.NOTIFICATION_TAG, notificationTag)
                .put(JsonKeys.IS_ACTIVE, isActive);
        }
    }

    @NonNull
    private final PreferenceManager preferenceManager;

    public PushMessageHistory(@NonNull PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public void addPush(@NonNull PushMessage pushMessage) {
        if (!CoreUtils.isEmpty(pushMessage.getNotificationId())) {
            savePushId(pushMessage.getNotificationId());
        }
        Filters filters = pushMessage.getFilters();
        if (filters != null && !CoreUtils.isEmpty(filters.getContentId())) {
            saveContentId(filters.getContentId());
        }
    }

    public void savePushTimestamp(@NonNull PushMessage pushMessage) {
        if (pushMessage.getNotification() != null) {
            saveShownTimeForChannelId(pushMessage.getNotification().getChannelId(), pushMessage.getTimestamp());
        }
    }

    //region push info
    public void savePushInfo(@NonNull final String pushId,
                             @NonNull final Integer notificationId,
                             @Nullable final String notificationTag,
                             boolean isActive) {
        final List<PushInfo> pushInfoList = getPushInfoList();
        final PushInfo pushInfo = new PushInfo(pushId, notificationId, notificationTag, isActive);
        pushInfoList.remove(pushInfo);
        pushInfoList.add(pushInfo);
        if (pushInfoList.size() > HISTORY_RECORDS_LIMIT) {
            pushInfoList.remove(0);
        }
        savePushInfoList(pushInfoList);
    }

    @NonNull
    public List<PushInfo> getPushInfoList() {
        String pushInfoListString = preferenceManager.getPushInfoList("");
        List<PushInfo> pushInfoList = new LinkedList<PushInfo>();
        try {
            JSONArray jsonArray = new JSONArray(pushInfoListString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pushId = jsonObject.getString(JsonKeys.PUSH_ID);
                Integer notificationId = jsonObject.getInt(JsonKeys.NOTIFICATION_ID);
                String notificationTag =
                    jsonObject.has(JsonKeys.NOTIFICATION_TAG) ? jsonObject.getString(JsonKeys.NOTIFICATION_TAG) : null;
                Boolean isActive =
                    jsonObject.has(JsonKeys.IS_ACTIVE) ? jsonObject.getBoolean(JsonKeys.IS_ACTIVE) : null;
                pushInfoList.add(new PushInfo(pushId, notificationId, notificationTag, isActive));
            }
        } catch (JSONException ignored) {
        }
        return pushInfoList;
    }

    @Nullable
    public PushInfo getPushInfoByNotificationTagAndNotificationId(
        @Nullable String notificationTag,
        int notificationId
    ) {
        List<PushInfo> pushInfoList = getPushInfoList();
        for (PushInfo pushInfo : pushInfoList) {
            if (Utils.equals(pushInfo.notificationTag, notificationTag)
                && Utils.equals(pushInfo.notificationId, notificationId)) {
                return pushInfo;
            }
        }
        return null;
    }

    @Nullable
    public PushInfo getPushInfoByPushId(@NonNull String pushId) {
        List<PushInfo> pushInfoList = getPushInfoList();
        for (PushInfo pushInfo : pushInfoList) {
            if (pushInfo.pushId.equals(pushId)) {
                return pushInfo;
            }
        }
        return null;
    }

    public void setPushActive(@NonNull String pushId, boolean isActive) {
        PushInfo pushInfo = getPushInfoByPushId(pushId);
        if (pushInfo != null) {
            savePushInfo(pushId, pushInfo.notificationId, pushInfo.notificationTag, isActive);
        }
    }

    private void savePushInfoList(@NonNull List<PushInfo> pushInfoList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (PushInfo pushInfo : pushInfoList) {
                jsonArray.put(pushInfo.toJson());
            }
        } catch (JSONException ignored) {
        }
        preferenceManager.savePushInfoList(jsonArray.toString());
    }
    //endregion

    //region push ids
    private void savePushId(@NonNull String pushId) {
        List<String> pushIds = getPushIds();
        pushIds.remove(pushId);
        pushIds.add(pushId);
        if (pushIds.size() > HISTORY_RECORDS_LIMIT) {
            pushIds.remove(0);
        }
        savePushIds(pushIds);
    }

    @NonNull
    public List<String> getPushIds() {
        String pushIdsString = preferenceManager.getPushIds("");
        List<String> pushIds = new LinkedList<String>();
        try {
            JSONArray jsonArray = new JSONArray(pushIdsString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pushId = jsonObject.getString(JsonKeys.PUSH_ID);
                pushIds.add(pushId);
            }
        } catch (JSONException ignored) {
        }
        return pushIds;
    }

    private void savePushIds(@NonNull List<String> pushIds) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (String pushId : pushIds) {
                JSONObject jsonObject = new JSONObject()
                    .put(JsonKeys.PUSH_ID, pushId);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException ignored) {
        }
        preferenceManager.savePushIds(jsonArray.toString());
    }
    //endregion

    //region content ids
    private void saveContentId(@NonNull String contentId) {
        List<String> contentIds = getContentIds();
        contentIds.remove(contentId);
        contentIds.add(contentId);
        if (contentIds.size() > HISTORY_RECORDS_LIMIT) {
            contentIds.remove(0);
        }
        saveContentIds(contentIds);
    }

    @NonNull
    public List<String> getContentIds() {
        String contentIdsString = preferenceManager.getContentIds("");
        List<String> contentIds = new LinkedList<String>();
        try {
            JSONArray jsonArray = new JSONArray(contentIdsString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pushId = jsonObject.getString(JsonKeys.CONTENT_ID);
                contentIds.add(pushId);
            }
        } catch (JSONException ignored) {
        }
        return contentIds;
    }

    private void saveContentIds(@NonNull List<String> contentIds) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (String pushId : contentIds) {
                JSONObject jsonObject = new JSONObject()
                    .put(JsonKeys.CONTENT_ID, pushId);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException ignored) {
        }
        preferenceManager.saveContentIds(jsonArray.toString());
    }
    //endregion

    //region shown times
    private void saveShownTimeForChannelId(@Nullable String channelId, long time) {
        List<Long> times = getShownTimesForChannelId(channelId);
        times.add(time);
        if (times.size() > HISTORY_RECORDS_LIMIT) {
            times.remove(0);
        }
        saveShownTimesForChannelId(channelId, times);
    }

    @NonNull
    public List<Long> getShownTimesForChannelId(@Nullable String channelId) {
        String timesString = preferenceManager.getShownTimesForChannelId(channelId, "");
        List<Long> times = new LinkedList<Long>();
        try {
            JSONArray jsonArray = new JSONArray(timesString);
            for (int i = 0; i < jsonArray.length(); i++) {
                times.add(jsonArray.getLong(i));
            }
        } catch (JSONException ignored) {
        }
        return times;
    }

    private void saveShownTimesForChannelId(@Nullable String channelId, @NonNull List<Long> times) {
        preferenceManager.saveShownTimesForChannelId(channelId, new JSONArray(times).toString());
    }
    //endregion

    public long getLastShownTimeForChannelId(@Nullable String channelId) {
        List<Long> times = getShownTimesForChannelId(channelId);
        return times.isEmpty() ? 0 : times.get(times.size() - 1);
    }
}
