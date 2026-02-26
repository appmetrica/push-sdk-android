package io.appmetrica.analytics.push.model;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.model.BasePushMessage;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import org.json.JSONObject;

/**
 * Parsed push message data.
 */
public class PushMessage {

    @NonNull
    private final Context context;
    @Nullable
    private final String notificationId;
    private final boolean silent;
    @Nullable
    private final String payload;
    @Nullable
    private final PushNotification notification;
    @NonNull
    private final Bundle bundle;
    @NonNull
    private final String transport;
    private final long timestamp;
    private final boolean ownPush;
    @Nullable
    private final Filters filters;
    @Nullable
    private final String pushIdToRemove;
    @Nullable
    private final LazyPushRequestInfo lazyPushRequestInfo;
    @Nullable
    private final Long timeToShowMillis;

    /**
     * Constructor for {@link PushMessage}.
     *
     * @param context {@link Context} for resource extraction
     * @param bundle {@link Bundle} with push message data
     */
    public PushMessage(@NonNull Context context, @NonNull Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
        this.transport = bundle.getString(CoreConstants.EXTRA_TRANSPORT, CoreConstants.Transport.UNKNOWN);
        JSONObject jsonObject = extractRootElement(bundle);
        ownPush = jsonObject != null;
        notificationId = JsonUtils.extractStringSafely(jsonObject, Constants.PushMessage.NOTIFICATION_ID);
        silent = JsonUtils.optBoolean(jsonObject, Constants.PushMessage.SILENT, false);
        payload = JsonUtils.extractStringSafely(jsonObject, Constants.PushMessage.PAYLOAD);
        notification = extractPushNotification(context, jsonObject);
        timestamp = notification == null ? System.currentTimeMillis() : notification.getWhen();
        filters = extractFilters(jsonObject);
        pushIdToRemove = JsonUtils.extractStringSafely(jsonObject, Constants.PushMessage.PUSH_ID_TO_REMOVE);
        lazyPushRequestInfo = extractLazyPush(jsonObject);
        timeToShowMillis = JsonUtils.extractLongSafely(jsonObject, Constants.PushMessage.TIME_TO_SHOW_MILLIS);
    }

    /**
     * Sets {@link Bundle} with push message data.
     * @param bundle {@link Bundle} with push message data
     * @return {@link JSONObject} with push message data
     */
    @Nullable
    public static JSONObject extractRootElement(@NonNull Bundle bundle) {
        return new BasePushMessage(bundle).getRoot();
    }

    @Nullable
    private PushNotification extractPushNotification(@NonNull Context context, @Nullable JSONObject pushMessageJson) {
        PushNotification pushNotification = null;
        if (pushMessageJson != null && pushMessageJson.has(Constants.PushMessage.NOTIFICATION)) {
            try {
                JSONObject notificationJson = pushMessageJson.getJSONObject(Constants.PushMessage.NOTIFICATION);
                pushNotification = new PushNotification(context, notificationJson);
            } catch (Throwable exception) {
                PublicLogger.INSTANCE.error(exception, "Error parsing push notification");
                TrackersHub.getInstance().reportError("Error parsing push notification", exception);
            }
        }
        return pushNotification;
    }

    @Nullable
    private Filters extractFilters(@Nullable JSONObject pushMessageJson) {
        Filters pushFilters = null;
        if (pushMessageJson != null && pushMessageJson.has(Constants.PushMessage.FILTERS)) {
            try {
                JSONObject filtersJson = pushMessageJson.getJSONObject(Constants.PushMessage.FILTERS);
                pushFilters = new Filters(filtersJson);
            } catch (Throwable e) {
                PublicLogger.INSTANCE.error(e, "Error parsing filters");
                TrackersHub.getInstance().reportError("Error parsing filters", e);
            }
        }
        return pushFilters;
    }

    @Nullable
    private LazyPushRequestInfo extractLazyPush(@Nullable JSONObject pushMessageJson) {
        LazyPushRequestInfo lazyPushRequestInfo = null;
        if (pushMessageJson != null && pushMessageJson.has(Constants.PushMessage.LAZY_PUSH)) {
            try {
                JSONObject lazyPushJson = pushMessageJson.getJSONObject(Constants.PushMessage.LAZY_PUSH);
                lazyPushRequestInfo = new LazyPushRequestInfo(lazyPushJson);
            } catch (Throwable e) {
                PublicLogger.INSTANCE.error(e, "Error parsing lazy push json");
                TrackersHub.getInstance().reportError("Error parsing lazy push json", e);
            }
        }
        return lazyPushRequestInfo;
    }

    /**
     * Checks whether push message refers to AppMetrica Push SDK.
     * Push message refers to AppMetrica Push SDK if {@link Bundle} contains correct Push SDK data.
     *
     * @return true if push message refers to AppMetrica Push SDK and false otherwise
     */
    public boolean isOwnPush() {
        return ownPush;
    }

    /**
     * Unique identifier of push message. It is used to identify push messages inside the SDK and for push events.
     *
     * @return identifier of push message
     */
    @Nullable
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * Returns true if {@link PushMessage} is silent and false otherwise.
     * @return true if {@link PushMessage} is silent and false otherwise
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * Returns payload.
     * @return payload
     */
    @Nullable
    public String getPayload() {
        return payload;
    }

    /**
     * Returns {@link PushNotification} object.
     * @return {@link PushNotification} object
     */
    @Nullable
    public PushNotification getNotification() {
        return notification;
    }

    /**
     * Returns timestamp.
     * @return timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns {@link Bundle} with data.
     * @return {@link Bundle} with data
     */
    @NonNull
    public Bundle getBundle() {
        return bundle;
    }

    /**
     * Returns {@link Filters} object.
     * @return {@link Filters} object
     */
    @Nullable
    public Filters getFilters() {
        return filters;
    }

    /**
     * Returns push ID to remove.
     * @return push ID to remove
     */
    @Nullable
    public String getPushIdToRemove() {
        return pushIdToRemove;
    }

    /**
     * Returns transport service title.
     * @return transport service title
     */
    @NonNull
    public String getTransport() {
        return transport;
    }

    /**
     * Returns {@link LazyPushRequestInfo} object.
     * @return {@link LazyPushRequestInfo} object
     */
    @Nullable
    public LazyPushRequestInfo getLazyPushRequestInfo() {
        return lazyPushRequestInfo;
    }

    /**
     * Returns time to show in milliseconds.
     * @return time to show in milliseconds
     */
    @Nullable
    public Long getTimeToShowMillis() {
        return timeToShowMillis;
    }

    /**
     * Merges {@link JSONObject} with current {@link PushMessage} and returns new {@link PushMessage}.
     *
     * @param json {@link JSONObject} with push message data
     * @return new {@link PushMessage} with merged data
     */
    @NonNull
    public PushMessage append(@Nullable JSONObject json) {
        if (json == null) {
            return this;
        }
        Bundle newBundle = new Bundle(bundle); // only first level
        JSONObject newConfig =
            JsonUtils.merge(extractRootElement(bundle), json.optJSONObject(CoreConstants.PushMessage.ROOT_ELEMENT));
        if (newConfig != null) {
            newBundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, newConfig.toString());
        }
        return new PushMessage(context, newBundle);
    }
}
