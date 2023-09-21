package io.appmetrica.analytics.push.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.utils.BitmapLoader;
import io.appmetrica.analytics.push.impl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describes push message notification.
 */
public class PushNotification {

    @Nullable
    private final String notificationTag;
    @Nullable
    private final Integer notificationId;
    @Nullable
    private final String category;
    @Nullable
    private final Boolean autoCancel;
    @Nullable
    private final Integer color;
    @Nullable
    private final String contentTitle;
    @Nullable
    private final String contentInfo;
    @Nullable
    private final String contentText;
    @Nullable
    private final String contentSubtext;
    @Nullable
    private final String ticker;
    @Nullable
    private final Integer defaults;
    @Nullable
    private final String group;
    @Nullable
    private final Boolean groupSummary;
    @Nullable
    private final LedLights ledLights;
    @Nullable
    private final Integer displayedNumber;
    @Nullable
    private final Boolean ongoing;
    @Nullable
    private final Boolean onlyAlertOnce;
    @Nullable
    private final Integer priority;
    private final long when;
    @Nullable
    private final Boolean showWhen;
    @Nullable
    private final String sortKey;
    @Nullable
    private final long[] vibrate;
    @Nullable
    private final Integer visibility;
    @Nullable
    private final String openActionUrl;
    @Nullable
    private final Integer iconResId;
    @Nullable
    private final Integer largeIconResId;
    @Nullable
    private final String largeIconUrl;
    @Nullable
    private Bitmap largeIcon;
    @Nullable
    private final Integer largeBitmapResId;
    @Nullable
    private final String largeBitmapUrl;
    @Nullable
    private Bitmap largeBitmap;
    private final boolean soundEnabled;
    @Nullable
    private final Integer soundResId;
    @Nullable
    private final AdditionalAction[] additionalActions;
    @Nullable
    private final String channelId;
    @Nullable
    private final Boolean explicitIntent;
    @Nullable
    private final Long notificationTtl;
    @Nullable
    private final Long timeToHideMillis;
    private final boolean useFlagActivityNewTask;
    @NonNull
    private final OpenType openType;

    @NonNull
    private final Context context;
    @NonNull
    private final BitmapLoader bitmapLoader;

    /**
     * Constructor for {@link PushNotification}.
     *
     * @param context {@link Context} to resource extraction
     * @param jsonObject {@link JSONObject} with push notification data
     */
    public PushNotification(@NonNull Context context, @NonNull JSONObject jsonObject) {
        this(context, jsonObject, new BitmapLoader(context));
    }

    @VisibleForTesting
    PushNotification(@NonNull Context context, @NonNull JSONObject jsonObject, @NonNull BitmapLoader bitmapLoader) {
        this.context = context;
        this.bitmapLoader = bitmapLoader;
        notificationTag = jsonObject.optString(Constants.PushMessage.Notification.TAG);
        notificationId = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Notification.ID);
        category = jsonObject.optString(Constants.PushMessage.Notification.CATEGORY);
        autoCancel = JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.Notification.AUTO_CANCEL);
        color = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Notification.COLOR);

        contentTitle = jsonObject.optString(Constants.PushMessage.Notification.CONTENT_TITLE);
        contentInfo = jsonObject.optString(Constants.PushMessage.Notification.CONTENT_INFO);
        contentText = jsonObject.optString(Constants.PushMessage.Notification.CONTENT_TEXT);
        contentSubtext = jsonObject.optString(Constants.PushMessage.Notification.CONTENT_SUBTEXT);
        ticker = jsonObject.optString(Constants.PushMessage.Notification.TICKER);
        defaults = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Notification.DEFAULTS);

        group = jsonObject.optString(Constants.PushMessage.Notification.GROUP);
        groupSummary = JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.Notification.GROUP_SUMMARY);

        ledLights = extractLedLights(jsonObject);

        displayedNumber = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.DISPLAYED_NUMBER);
        ongoing = JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.Notification.ONGOING);
        onlyAlertOnce = JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.Notification.ONLY_ALERT_ONCE);
        priority = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Notification.PRIORITY);
        when = jsonObject.optLong(Constants.PushMessage.Notification.WHEN, System.currentTimeMillis());
        showWhen = JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.Notification.SHOW_WHEN);
        sortKey = jsonObject.optString(Constants.PushMessage.Notification.SORT_KEY);
        vibrate = extractLongArraySafely(jsonObject, Constants.PushMessage.Notification.VIBRATE);
        visibility = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Notification.VISIBILITY);
        iconResId = Utils.wrapResId(context, jsonObject.optString(Constants.PushMessage.Notification.ICON_RES_ID));
        largeIconUrl = jsonObject.optString(Constants.PushMessage.Notification.LARGE_ICON_URL);
        largeBitmapUrl = jsonObject.optString(Constants.PushMessage.Notification.LARGE_BITMAP_URL);
        soundEnabled = jsonObject.optInt(Constants.PushMessage.Notification.SOUND_ENABLED, 0) == 1;
        soundResId =
            Utils.wrapSoundResId(context, jsonObject.optString(Constants.PushMessage.Notification.SOUND_RES_ID));
        openActionUrl = jsonObject.optString(Constants.PushMessage.Notification.OPEN_ACTION);
        additionalActions = extractAdditionalActions(context, jsonObject);
        channelId = jsonObject.optString(Constants.PushMessage.Notification.CHANNEL_ID);
        explicitIntent = JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.Notification.EXPLICIT_INTENT);
        largeIconResId =
            Utils.wrapResId(context, jsonObject.optString(Constants.PushMessage.Notification.LARGE_ICON_ID));
        largeBitmapResId = Utils.wrapResId(context,
            jsonObject.optString(Constants.PushMessage.Notification.LARGE_BITMAP_ID));
        notificationTtl = JsonUtils.extractLongSafely(jsonObject, Constants.PushMessage.Notification.NOTIFICATION_TTL);
        timeToHideMillis =
            JsonUtils.extractLongSafely(jsonObject, Constants.PushMessage.Notification.TIME_TO_HIDE_MILLIS);
        useFlagActivityNewTask = JsonUtils.optBoolean(jsonObject,
            Constants.PushMessage.Notification.USE_ACTIVITY_NEW_TASK_FLAG, true);
        openType = extractOpenType(jsonObject);
    }

    @Nullable
    private LedLights extractLedLights(@NonNull JSONObject jsonObject) {
        if (jsonObject.has(Constants.PushMessage.Notification.LED_LIGHTS)) {
            try {
                return new LedLights(jsonObject.getJSONObject(Constants.PushMessage.Notification.LED_LIGHTS));
            } catch (JSONException ignored) {
            }
        }
        return null;
    }

    @Nullable
    private AdditionalAction[] extractAdditionalActions(@NonNull Context context, @NonNull JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.PushMessage.Notification.ADDITIONAL_ACTIONS);
            AdditionalAction[] result = new AdditionalAction[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                result[i] = new AdditionalAction(context, jsonArray.getJSONObject(i));
            }
            return result;
        } catch (JSONException ignored) {
            return null;
        }
    }

    @Nullable
    private static long[] extractLongArraySafely(@NonNull JSONObject jsonObject, @NonNull String key) {
        long[] result = null;
        if (jsonObject.has(key)) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                result = new long[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    result[i] = jsonArray.getLong(i);
                }
            } catch (JSONException e) {
                result = null;
            }
        }
        return result;
    }

    @NonNull
    private OpenType extractOpenType(@NonNull JSONObject jsonObject) {
        OpenType type = OpenType.UNKNOWN;
        Integer typeInt = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.OPEN_TYPE);
        if (typeInt != null) {
            type = OpenType.fromValue(typeInt);
        }
        return type;
    }

    /**
     * @return tag
     */
    @Nullable
    public String getNotificationTag() {
        return notificationTag;
    }

    /**
     * @return ID
     */
    @Nullable
    public Integer getNotificationId() {
        return notificationId;
    }

    /**
     * @return category
     */
    @Nullable
    public String getCategory() {
        return category;
    }

    /**
     * @return auto cancel
     */
    @Nullable
    public Boolean getAutoCancel() {
        return autoCancel;
    }

    /**
     * @return color
     */
    @Nullable
    public Integer getColor() {
        return color;
    }

    /**
     * @return content title
     */
    @Nullable
    public String getContentTitle() {
        return contentTitle;
    }

    /**
     * @return content info
     */
    @Nullable
    public String getContentInfo() {
        return contentInfo;
    }

    /**
     * @return content text
     */
    @Nullable
    public String getContentText() {
        return contentText;
    }

    /**
     * @return content subtext
     */
    @Nullable
    public String getContentSubtext() {
        return contentSubtext;
    }

    /**
     * @return toker
     */
    @Nullable
    public String getTicker() {
        return ticker;
    }

    /**
     * @return defaults
     */
    @Nullable
    public Integer getDefaults() {
        return defaults;
    }

    /**
     * @return group summary
     */
    @Nullable
    public Boolean getGroupSummary() {
        return groupSummary;
    }

    /**
     * @return group
     */
    @Nullable
    public String getGroup() {
        return group;
    }

    /**
     * @return led lights
     */
    @Nullable
    public LedLights getLedLights() {
        return ledLights;
    }

    /**
     * @return displayed number
     */
    @Nullable
    public Integer getDisplayedNumber() {
        return displayedNumber;
    }

    /**
     * @return ongoing
     */
    @Nullable
    public Boolean getOngoing() {
        return ongoing;
    }

    /**
     * @return only alert once
     */
    @Nullable
    public Boolean getOnlyAlertOnce() {
        return onlyAlertOnce;
    }

    /**
     * @return priority
     */
    @Nullable
    public Integer getPriority() {
        return priority;
    }

    /**
     * @return when
     */
    @NonNull
    public Long getWhen() {
        return when;
    }

    /**
     * @return show when
     */
    @Nullable
    public Boolean getShowWhen() {
        return showWhen;
    }

    /**
     * @return sort key
     */
    @Nullable
    public String getSortKey() {
        return sortKey;
    }

    /**
     * @return vibrate pattern
     */
    @Nullable
    public long[] getVibrate() {
        return vibrate;
    }

    /**
     * @return visibility
     */
    @Nullable
    public Integer getVisibility() {
        return visibility;
    }

    /**
     * @return small icon resource ID
     */
    @Nullable
    public Integer getIconResId() {
        return iconResId;
    }

    /**
     * @return large icon resource ID
     */
    @Nullable
    public Integer getLargeIconResId() {
        return largeIconResId;
    }

    /**
     * @return large icon URL
     */
    @Nullable
    public String getLargeIconUrl() {
        return largeIconUrl;
    }

    /**
     * @return TTL
     */
    @Nullable
    public Long getNotificationTtl() {
        return notificationTtl;
    }

    /**
     * @return large icon
     */
    @Nullable
    public Bitmap getLargeIcon() {
        if (largeIcon == null) {
            float iconWidth = Utils.getNotificationLargeIconWidth(context);
            float iconHeight = Utils.getNotificationLargeIconHeight(context);
            largeIcon = getBitmap(context, bitmapLoader, largeIconResId, largeIconUrl, iconWidth, iconHeight);
        }
        return largeIcon;
    }

    /**
     * @return large bitmap resource ID
     */
    @Nullable
    public Integer getLargeBitmapResId() {
        return largeBitmapResId;
    }

    /**
     * @return large bitmap URL
     */
    @Nullable
    public String getLargeBitmapUrl() {
        return largeBitmapUrl;
    }

    /**
     * @return large bitmap
     */
    @Nullable
    public Bitmap getLargeBitmap() {
        if (largeBitmap == null) {
            largeBitmap = getBitmap(context, bitmapLoader, largeBitmapResId, largeBitmapUrl,
                BitmapLoader.UNDEFINED_WIDTH, BitmapLoader.UNDEFINED_HEIGHT);
        }
        return largeBitmap;
    }

    /**
     * @return is sound enabled
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * @return sound resource id
     */
    @Nullable
    public Integer getSoundResId() {
        return soundResId;
    }

    /**
     * @return sound URI
     */
    @Nullable
    public Uri getSoundUri() {
        if (soundResId != null) {
            Resources resources = context.getResources();
            return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(soundResId))
                .appendPath(resources.getResourceTypeName(soundResId))
                .appendPath(resources.getResourceEntryName(soundResId))
                .build();
        } else {
            return null;
        }
    }

    /**
     * @return open action URL
     */
    @Nullable
    public String getOpenActionUrl() {
        return openActionUrl;
    }

    /**
     * @return additional actions
     */
    @Nullable
    public AdditionalAction[] getAdditionalActions() {
        return additionalActions;
    }

    /**
     * @return channel ID
     */
    @Nullable
    public String getChannelId() {
        return channelId;
    }

    /**
     * @return whether to use explicit intent
     */
    @Nullable
    public Boolean getExplicitIntent() {
        return explicitIntent;
    }

    @Nullable
    private static Bitmap getBitmap(@NonNull Context context,
                                    @NonNull BitmapLoader bitmapLoader,
                                    @Nullable Integer resId,
                                    @Nullable String url,
                                    float with,
                                    float height) {
        Bitmap bitmap = null;
        if (resId != null) {
            PublicLogger.i("Get bitmap from resources with id: %d", resId);
            bitmap = Utils.getBitmapFromResources(context, resId, with, height);
        }
        if (bitmap == null && !CoreUtils.isEmpty(url)) {
            PublicLogger.i("Download bitmap for url: %s", url);
            bitmap = bitmapLoader.get(context, url, with, height);
        }
        return bitmap;
    }

    /**
     * @return time to hide in milliseconds
     */
    @Nullable
    public Long getTimeToHideMillis() {
        return timeToHideMillis;
    }

    /**
     * @return whether to use {@link Intent#FLAG_ACTIVITY_NEW_TASK} flag
     */
    public boolean getUseFlagActivityNewTask() {
        return useFlagActivityNewTask;
    }

    /**
     * @return open type
     */
    public OpenType getOpenType() {
        return openType;
    }
}
