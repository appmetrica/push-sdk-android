package io.appmetrica.analytics.push.model;

import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.impl.Constants;
import org.json.JSONObject;

/**
 * Parsed location info.
 */
public class LocationRequestInfo {

    @Nullable
    private final String locationProvider;
    @Nullable
    private final Long requestTimeoutSeconds;
    @Nullable
    private final Long minRecency;
    @Nullable
    private final Integer minAccuracy;

    /**
     * Constructor for {@link LocationRequestInfo}.
     *
     * @param jsonObject {@link JSONObject} with location data
     */
    public LocationRequestInfo(@NonNull JSONObject jsonObject) {
        locationProvider = getLocationProviderById(
            JsonUtils.extractIntegerSafely(jsonObject, Constants.PushMessage.LazyPush.LocationRequestInfo.PROVIDER)
        );
        requestTimeoutSeconds = JsonUtils
            .extractLongSafely(jsonObject, Constants.PushMessage.LazyPush.LocationRequestInfo.REQUEST_TIMEOUT_SECONDS);
        minRecency =
            JsonUtils.extractLongSafely(jsonObject, Constants.PushMessage.LazyPush.LocationRequestInfo.MIN_RECENCY);
        minAccuracy = JsonUtils
            .extractIntegerSafely(jsonObject, Constants.PushMessage.LazyPush.LocationRequestInfo.MIN_ACCURACY);
    }

    /**
     * Returns location provider.
     * @return location provider
     */
    @Nullable
    public String getProvider() {
        return locationProvider;
    }

    /**
     * Returns request timeout in seconds.
     * @return request timeout in seconds
     */
    @Nullable
    public Long getRequestTimeoutSeconds() {
        return requestTimeoutSeconds;
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

    @Nullable
    private String getLocationProviderById(@Nullable final Integer id) {
        if (id == null) {
            return null;
        }
        switch (id) {
            case 0: return LocationManager.PASSIVE_PROVIDER;
            case 1: return LocationManager.NETWORK_PROVIDER;
            case 2: return LocationManager.GPS_PROVIDER;
            default: return null;
        }
    }
}
