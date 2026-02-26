package io.appmetrica.analytics.push.model;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.utils.Utils;
import org.json.JSONObject;

/**
 * Parsed additional action data.
 */
public class AdditionalAction {

    @Nullable
    private final String id;
    @Nullable
    private final String title;
    @Nullable
    private final String actionUrl;
    @Nullable
    private final Integer iconResId;
    @Nullable
    private final Boolean hideQuickControlPanel;
    @Nullable
    private final Boolean autoCancel;
    @Nullable
    private final Boolean explicitIntent;
    @Nullable
    private final AdditionalActionType additionalActionType;
    @Nullable
    private final String label;
    @Nullable
    private final Long hideAfterSecond;
    @NonNull
    private final OpenType openType;
    private final boolean useFlagActivityNewTask;

    /**
     * Constructor for {@link AdditionalAction}.
     *
     * @param context {@link Context} object
     * @param jsonObject {@link JSONObject} with additional action data
     */
    public AdditionalAction(@NonNull Context context, @NonNull JSONObject jsonObject) {
        id = jsonObject.optString(Constants.PushMessage.Notification.AdditionalAction.ID);
        title = jsonObject.optString(Constants.PushMessage.Notification.AdditionalAction.TITLE);
        actionUrl = jsonObject.optString(Constants.PushMessage.Notification.AdditionalAction.ACTION_URL);
        iconResId = Utils.wrapResId(context,
            jsonObject.optString(Constants.PushMessage.Notification.AdditionalAction.ICON_RES_ID));
        hideQuickControlPanel = JsonUtils.extractBooleanSafely(jsonObject,
            Constants.PushMessage.Notification.AdditionalAction.HIDE_QUICK_CONTROL_PANEL);
        autoCancel = JsonUtils.extractBooleanSafely(jsonObject,
            Constants.PushMessage.Notification.AdditionalAction.AUTO_CANCEL);
        explicitIntent = JsonUtils.extractBooleanSafely(jsonObject,
            Constants.PushMessage.Notification.AdditionalAction.EXPLICIT_INTENT);
        additionalActionType = extractType(context, jsonObject);
        label = jsonObject.optString(Constants.PushMessage.Notification.AdditionalAction.LABEL);
        hideAfterSecond =
            JsonUtils.extractLongSafely(
                jsonObject,
                Constants.PushMessage.Notification.AdditionalAction.HIDE_AFTER_SECONDS
            );
        openType = extractOpenType(jsonObject);
        useFlagActivityNewTask = JsonUtils.optBoolean(jsonObject,
            Constants.PushMessage.Notification.AdditionalAction.USE_ACTIVITY_NEW_TASK_FLAG, true);
    }

    /**
     * Returns action ID.
     * @return action ID
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     * Returns title.
     * @return title
     */
    @Nullable
    public String getTitle() {
        return title;
    }

    /**
     * Returns action URL.
     * @return action URL
     */
    @Nullable
    public String getActionUrl() {
        return actionUrl;
    }

    /**
     * Returns icon resource ID.
     * @return icon resource ID
     */
    @Nullable
    public Integer getIconResId() {
        return iconResId;
    }

    /**
     * Returns whether to hide quick control panel.
     * @return whether to hide quick control panel
     */
    @Nullable
    public Boolean getHideQuickControlPanel() {
        return hideQuickControlPanel;
    }

    /**
     * Returns whether to auto cancel.
     * @return whether to auto cancel
     */
    @Nullable
    public Boolean getAutoCancel() {
        return autoCancel;
    }

    /**
     * Returns whether to use explicit intent.
     * @return whether to use explicit intent
     */
    @Nullable
    public Boolean getExplicitIntent() {
        return explicitIntent;
    }

    /**
     * Returns type.
     * @return type
     */
    @Nullable
    public AdditionalActionType getType() {
        return additionalActionType;
    }

    /**
     * Returns label.
     * @return label
     */
    @Nullable
    public String getLabel() {
        return label;
    }

    /**
     * Returns hide after in seconds.
     * @return hide after in seconds
     */
    @Nullable
    public Long getHideAfterSecond() {
        return hideAfterSecond;
    }

    /**
     * Returns open type.
     * @return open type
     */
    @NonNull
    public OpenType getOpenType() {
        return openType;
    }

    /**
     * Returns whether to use {@link Intent#FLAG_ACTIVITY_NEW_TASK} flag.
     * @return whether to use {@link Intent#FLAG_ACTIVITY_NEW_TASK} flag
     */
    public boolean getUseFlagActivityNewTask() {
        return useFlagActivityNewTask;
    }

    @Nullable
    private AdditionalActionType extractType(@NonNull Context context, @NonNull JSONObject jsonObject) {
        AdditionalActionType additionalActionType = null;
        Integer typeInt = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.AdditionalAction.TYPE);
        if (typeInt != null) {
            additionalActionType = AdditionalActionType.fromValue(typeInt);
        }
        return additionalActionType;
    }

    @NonNull
    private OpenType extractOpenType(@NonNull JSONObject jsonObject) {
        OpenType type = OpenType.UNKNOWN;
        Integer typeInt = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.AdditionalAction.OPEN_TYPE);
        if (typeInt != null) {
            type = OpenType.fromValue(typeInt);
        }
        return type;
    }
}
