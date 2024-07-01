package io.appmetrica.analytics.push.coreutils.internal.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonUtils {

    private JsonUtils() {
    }

    public static boolean optBoolean(@Nullable JSONObject jsonObject, @NonNull String key, boolean defValue) {
        return jsonObject == null ? defValue : jsonObject.optBoolean(key, defValue);
    }

    @Nullable
    public static String extractStringSafely(@Nullable JSONObject jsonObject, @NonNull String key) {
        return jsonObject == null ? null : jsonObject.optString(key, null);
    }

    @Nullable
    public static Boolean extractBooleanSafely(@Nullable JSONObject jsonObject, @NonNull String key) {
        Boolean result = null;
        if (jsonObject != null && jsonObject.has(key)) {
            try {
                result = jsonObject.getBoolean(key);
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    @Nullable
    public static Integer extractIntegerSafely(@Nullable JSONObject jsonObject, @NonNull String key) {
        Integer result = null;
        if (jsonObject != null && jsonObject.has(key)) {
            try {
                result = jsonObject.getInt(key);
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    @Nullable
    public static Long extractLongSafely(@Nullable JSONObject jsonObject, @NonNull String key) {
        Long result = null;
        if (jsonObject != null && jsonObject.has(key)) {
            try {
                result = jsonObject.getLong(key);
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    @Nullable
    public static JSONObject merge(@Nullable JSONObject json1, @Nullable JSONObject json2) {
        try {
            if (json1 == null && json2 == null) {
                return null;
            } else if (json2 == null) {
                return new JSONObject(json1.toString());
            } else if (json1 == null) {
                return new JSONObject(json2.toString());
            }
            final JSONObject newJson = new JSONObject(json1.toString());
            for (Iterator<String> it = json2.keys(); it.hasNext(); ) {
                String key = it.next();
                Object value = json2.opt(key);
                if (value instanceof JSONObject) {
                    JSONObject curValue = newJson.optJSONObject(key);
                    Object newValue = merge(curValue, (JSONObject) value);
                    if (newValue == null) {
                        throw new JSONException(
                            String.format("Failed to marge json %s with %s for key '%s'", key, curValue, value)
                        );
                    }
                    newJson.put(key, newValue);
                } else {
                    newJson.put(key, value);
                }
            }
            return newJson;
        } catch (Throwable e) {
            PublicLogger.INSTANCE.error(e, "Failed to merge json %s with %s", json1, json2);
            return null;
        }
    }
}
