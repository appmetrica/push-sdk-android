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
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.utils.BitmapLoader;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
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
     * Returns tag.
     * @return tag
     */
    @Nullable
    public String getNotificationTag() {
        return notificationTag;
    }

    /**
     * Notification ID. Sending another push with same notification ID will lead to
     * replacing the notification.
     *
     * @return notification ID
     */
    @Nullable
    public Integer getNotificationId() {
        return notificationId;
    }

    /**
     * Returns category.
     * @return category
     */
    @Nullable
    public String getCategory() {
        return category;
    }

    /**
     * Returns auto cancel.
     * @return auto cancel
     */
    @Nullable
    public Boolean getAutoCancel() {
        return autoCancel;
    }

    /**
     * Returns color.
     * @return color
     */
    @Nullable
    public Integer getColor() {
        return color;
    }

    /**
     * Returns content title.
     * @return content title
     */
    @Nullable
    public String getContentTitle() {
        return contentTitle;
    }

    /**
     * Returns content info.
     * @return content info
     */
    @Nullable
    public String getContentInfo() {
        return contentInfo;
    }

    /**
     * Returns content text.
     * @return content text
     */
    @Nullable
    public String getContentText() {
        return contentText;
    }

    /**
     * Returns content subtext.
     * @return content subtext
     */
    @Nullable
    public String getContentSubtext() {
        return contentSubtext;
    }

    /**
     * Returns toker.
     * @return toker
     */
    @Nullable
    public String getTicker() {
        return ticker;
    }

    /**
     * Returns defaults.
     * @return defaults
     */
    @Nullable
    public Integer getDefaults() {
        return defaults;
    }

    /**
     * Returns group summary.
     * @return group summary
     */
    @Nullable
    public Boolean getGroupSummary() {
        return groupSummary;
    }

    /**
     * Returns group.
     * @return group
     */
    @Nullable
    public String getGroup() {
        return group;
    }

    /**
     * Returns led lights.
     * @return led lights
     */
    @Nullable
    public LedLights getLedLights() {
        return ledLights;
    }

    /**
     * Returns displayed number.
     * @return displayed number
     */
    @Nullable
    public Integer getDisplayedNumber() {
        return displayedNumber;
    }

    /**
     * Returns ongoing.
     * @return ongoing
     */
    @Nullable
    public Boolean getOngoing() {
        return ongoing;
    }

    /**
     * Returns only alert once.
     * @return only alert once
     */
    @Nullable
    public Boolean getOnlyAlertOnce() {
        return onlyAlertOnce;
    }

    /**
     * Returns priority.
     * @return priority
     */
    @Nullable
    public Integer getPriority() {
        return priority;
    }

    /**
     * Returns when.
     * @return when
     */
    @NonNull
    public Long getWhen() {
        return when;
    }

    /**
     * Returns show when.
     * @return show when
     */
    @Nullable
    public Boolean getShowWhen() {
        return showWhen;
    }

    /**
     * Returns sort key.
     * @return sort key
     */
    @Nullable
    public String getSortKey() {
        return sortKey;
    }

    /**
     * Returns vibrate pattern.
     * @return vibrate pattern
     */
    @Nullable
    public long[] getVibrate() {
        return vibrate;
    }

    /**
     * Returns visibility.
     * @return visibility
     */
    @Nullable
    public Integer getVisibility() {
        return visibility;
    }

    /**
     * Returns small icon resource ID.
     * @return small icon resource ID
     */
    @Nullable
    public Integer getIconResId() {
        return iconResId;
    }

    /**
     * Returns large icon resource ID.
     * @return large icon resource ID
     */
    @Nullable
    public Integer getLargeIconResId() {
        return largeIconResId;
    }

    /**
     * Returns large icon URL.
     * @return large icon URL
     */
    @Nullable
    public String getLargeIconUrl() {
        return largeIconUrl;
    }

    /**
     * Returns tTL.
     * @return TTL
     */
    @Nullable
    public Long getNotificationTtl() {
        return notificationTtl;
    }

    /**
     * Returns large icon.
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
     * Returns large bitmap resource ID.
     * @return large bitmap resource ID
     */
    @Nullable
    public Integer getLargeBitmapResId() {
        return largeBitmapResId;
    }

    /**
     * Returns large bitmap URL.
     * @return large bitmap URL
     */
    @Nullable
    public String getLargeBitmapUrl() {
        return largeBitmapUrl;
    }

    /**
     * Returns large bitmap.
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
     * Returns is sound enabled.
     * @return is sound enabled
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Returns sound resource id.
     * @return sound resource id
     */
    @Nullable
    public Integer getSoundResId() {
        return soundResId;
    }

    /**
     * Returns sound URI.
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
     * Returns open action URL.
     * @return open action URL
     */
    @Nullable
    public String getOpenActionUrl() {
        return openActionUrl;
    }

    /**
     * Returns additional actions.
     * @return additional actions
     */
    @Nullable
    public AdditionalAction[] getAdditionalActions() {
        return additionalActions;
    }

    /**
     * Returns channel ID.
     * @return channel ID
     */
    @Nullable
    public String getChannelId() {
        return channelId;
    }

    /**
     * Returns whether to use explicit intent.
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
            PublicLogger.INSTANCE.info("Get bitmap from resources with id: %d", resId);
            bitmap = Utils.getBitmapFromResources(context, resId, with, height);
        }
        if (bitmap == null && !CoreUtils.isEmpty(url)) {
            PublicLogger.INSTANCE.info("Download bitmap for url: %s", url);
            bitmap = bitmapLoader.get(context, url, with, height);
        }
        return bitmap;
    }

    /**
     * Returns time to hide in milliseconds.
     * @return time to hide in milliseconds
     */
    @Nullable
    public Long getTimeToHideMillis() {
        return timeToHideMillis;
    }

    /**
     * Returns whether to use {@link Intent#FLAG_ACTIVITY_NEW_TASK} flag.
     * @return whether to use {@link Intent#FLAG_ACTIVITY_NEW_TASK} flag
     */
    public boolean getUseFlagActivityNewTask() {
        return useFlagActivityNewTask;
    }

    /**
     * Returns open type.
     * @return open type
     */
    public OpenType getOpenType() {
        return openType;
    }
}
