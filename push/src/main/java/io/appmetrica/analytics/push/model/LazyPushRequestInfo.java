package io.appmetrica.analytics.push.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.Constants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parsed lazy push request info.
 */
public class LazyPushRequestInfo {

    @Nullable
    private final String url;
    @Nullable
    private final Boolean useCurPushAsFallback;
    @Nullable
    private final Map<String, String> headers;
    @Nullable
    private final LocationRequestInfo locationRequestInfo;
    @Nullable
    private final long[] retryStrategySeconds;

    /**
     * Constructor for {@link LazyPushRequestInfo}.
     *
     * @param jsonObject {@link JSONObject} with lazy push reeust info data
     */
    public LazyPushRequestInfo(@NonNull JSONObject jsonObject) {
        url = JsonUtils.extractStringSafely(jsonObject, Constants.PushMessage.LazyPush.URL);
        useCurPushAsFallback =
            JsonUtils.extractBooleanSafely(jsonObject, Constants.PushMessage.LazyPush.USE_CUR_PUSH_AS_FALLBACK);
        headers = extractHeaders(jsonObject);
        locationRequestInfo = extractLocationRequestInfo(jsonObject);
        retryStrategySeconds =
            extractLongArraySafely(jsonObject, Constants.PushMessage.LazyPush.RETRY_STRATEGY_SECONDS);
    }

    @Nullable
    private Map<String, String> extractHeaders(@NonNull JSONObject jsonObject) {
        JSONObject headersJson = jsonObject.optJSONObject(Constants.PushMessage.LazyPush.HEADERS);
        if (headersJson == null) {
            return null;
        }
        try {
            final Map<String, String> headers = new HashMap<String, String>(headersJson.length());
            for (Iterator<String> it = headersJson.keys(); it.hasNext(); ) {
                String key = it.next();
                headers.put(key, headersJson.optString(key));
            }
            return Collections.unmodifiableMap(headers);
        } catch (Throwable e) {
            PublicLogger.e(e, "Error parsing lazy push headers");
            TrackersHub.getInstance().reportError("Error parsing lazy push headers", e);
            return null;
        }
    }

    @Nullable
    private LocationRequestInfo extractLocationRequestInfo(@NonNull JSONObject jsonObject) {
        LocationRequestInfo info = null;
        if (jsonObject.has(Constants.PushMessage.LazyPush.LOCATION_REQUEST_INFO)) {
            try {
                JSONObject locationJson =
                    jsonObject.getJSONObject(Constants.PushMessage.LazyPush.LOCATION_REQUEST_INFO);
                info = new LocationRequestInfo(locationJson);
            } catch (JSONException e) {
                PublicLogger.e(e, "Error parsing location request info for lazy push");
                TrackersHub.getInstance().reportError("Error parsing location request info for lazy push", e);
            }
        }
        return info;
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
            } catch (Throwable e) {
                result = null;
            }
        }
        return result;
    }

    /**
     * @return URL
     */
    @Nullable
    public String getUrl() {
        return url;
    }

    /**
     * @return whether to use current {@link PushMessage} as fallback
     */
    @Nullable
    public Boolean getUseCurPushAsFallback() {
        return useCurPushAsFallback;
    }

    /**
     * @return headers pattern
     */
    @Nullable
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Contains requirements for the location that Push SDK receives for lazy push to work.
     *
     * @return location request info
     */
    @Nullable
    public LocationRequestInfo getLocationRequestInfo() {
        return locationRequestInfo;
    }

    /**
     * @return retry strategy in seconds
     */
    @Nullable
    public long[] getRetryStrategySeconds() {
        return retryStrategySeconds;
    }
}
