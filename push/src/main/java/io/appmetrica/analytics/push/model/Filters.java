package io.appmetrica.analytics.push.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Possible filters that {@link PushMessage} can have.
 */
public class Filters {

    @Nullable
    private final Integer maxPushPerDay;
    @Nullable
    private final Integer onePushPerPeriodMinutes;
    @Nullable
    private final String passportUid;
    @Nullable
    private final Integer loginFilterType;
    @Nullable
    private final Coordinates coordinates;
    @Nullable
    private final Long minRecency;
    @Nullable
    private final Integer minAccuracy;
    @Nullable
    private final Boolean isPassiveLocation;
    @Nullable
    private final Integer minVersionCode;
    @Nullable
    private final Integer maxVersionCode;
    @Nullable
    private final Integer minAndroidApiLevel;
    @Nullable
    private final Integer maxAndroidApiLevel;
    @Nullable
    private final String contentId;

    /**
     * Constructor for {@link Filters}.
     *
     * @param jsonObject {@link JSONObject} with filters data
     */
    public Filters(@NonNull JSONObject jsonObject) {
        maxPushPerDay = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Filters.MAX_PUSH_PER_DAY);
        onePushPerPeriodMinutes = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Filters.ONE_PUSH_PER_PERIOD_MINUTES);
        passportUid = JsonUtils.extractStringSafely(jsonObject, Constants.PushMessage.Filters.PASSPORT_UID);
        loginFilterType = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Filters.LOGIN_FILTER_TYPE);
        coordinates = extractCoordinates(jsonObject);
        minRecency = JsonUtils.extractLongSafely(jsonObject, Constants.PushMessage.Filters.MIN_RECENCY);
        minAccuracy = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Filters.MIN_ACCURACY);
        isPassiveLocation = JsonUtils.extractBooleanSafely(jsonObject,
            Constants.PushMessage.Filters.IS_PASSIVE_LOCATION);
        minVersionCode = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Filters.MIN_VERSION_CODE);
        maxVersionCode = JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.Filters.MAX_VERSION_CODE);
        minAndroidApiLevel = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Filters.MIN_ANDROID_API_LEVEL);
        maxAndroidApiLevel = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Filters.MAX_ANDROID_API_LEVEL);
        contentId = JsonUtils.extractStringSafely(jsonObject, Constants.PushMessage.Filters.CONTENT_ID);
    }

    /**
     * Returns maximum pushes count per day.
     * @return maximum pushes count per day
     */
    @Nullable
    public Integer getMaxPushPerDay() {
        return maxPushPerDay;
    }

    /**
     * Returns one push per period in minutes.
     * @return one push per period in minutes
     */
    @Nullable
    public Integer getOnePushPerPeriodMinutes() {
        return onePushPerPeriodMinutes;
    }

    /**
     * Returns passport UID.
     * @return passport UID
     */
    @Nullable
    public String getPassportUid() {
        return passportUid;
    }

    /**
     * Returns login filter type.
     * @return login filter type
     */
    @Nullable
    public Integer getLoginFilterType() {
        return loginFilterType;
    }

    /**
     * Returns coordinates.
     * @return coordinates
     */
    @Nullable
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns minimal recency.
     * @return minimal recency
     */
    @Nullable
    public Long getMinRecency() {
        return minRecency;
    }

    /**
     * Returns minimal accuracy.
     * @return minimal accuracy
     */
    @Nullable
    public Integer getMinAccuracy() {
        return minAccuracy;
    }

    /**
     * Returns whether to use passive location.
     * @return whether to use passive location
     */
    @Nullable
    public Boolean getPassiveLocation() {
        return isPassiveLocation;
    }

    /**
     * Returns minimal version code.
     * @return minimal version code
     */
    @Nullable
    public Integer getMinVersionCode() {
        return minVersionCode;
    }

    /**
     * Returns maximal version code.
     * @return maximal version code
     */
    @Nullable
    public Integer getMaxVersionCode() {
        return maxVersionCode;
    }

    /**
     * Returns minimal Android API Level.
     * @return minimal Android API Level
     */
    @Nullable
    public Integer getMinAndroidApiLevel() {
        return minAndroidApiLevel;
    }

    /**
     * Returns maximal Android API Level.
     * @return maximal Android API Level
     */
    @Nullable
    public Integer getMaxAndroidApiLevel() {
        return maxAndroidApiLevel;
    }

    /**
     * Returns content ID.
     * @return content ID
     */
    @Nullable
    public String getContentId() {
        return contentId;
    }

    @Nullable
    private static Coordinates extractCoordinates(@NonNull JSONObject jsonObject) {
        Coordinates coordinates = null;
        if (jsonObject.has(Constants.PushMessage.Filters.COORDINATES)) {
            try {
                JSONObject coordinatesJson = jsonObject.getJSONObject(Constants.PushMessage.Filters.COORDINATES);
                coordinates = new Coordinates(coordinatesJson);
            } catch (JSONException e) {
                PublicLogger.INSTANCE.error(e, "Error parsing coordinates");
                TrackersHub.getInstance().reportError("Error parsing coordinates", e);
            }
        }
        return coordinates;
    }
}
