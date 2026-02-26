package io.appmetrica.analytics.push.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils;
import io.appmetrica.analytics.push.impl.Constants;
import org.json.JSONObject;

/**
 * Describes values that is user in {@link NotificationCompat.Builder#setLights(int, int, int)} method.
 */
public class LedLights {

    @Nullable
    private final Integer color;
    @Nullable
    private final Integer onMs;
    @Nullable
    private final Integer offMs;

    /**
     * Constructor for {@link LedLights}.
     *
     * @param jsonObject {@link JSONObject} with led lights data
     */
    public LedLights(@NonNull JSONObject jsonObject) {
        color = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.LedLights.LED_LIGHTS_COLOR);
        onMs = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.LedLights.LED_LIGHTS_ON_MS);
        offMs = JsonUtils.extractIntegerSafely(jsonObject,
            Constants.PushMessage.Notification.LedLights.LED_LIGHTS_OFF_MS);
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
     * Returns onMs.
     * @return onMs
     */
    @Nullable
    public Integer getOnMs() {
        return onMs;
    }

    /**
     * Returns offMs.
     * @return offMs
     */
    @Nullable
    public Integer getOffMs() {
        return offMs;
    }

    /**
     * Checks if parsed data is valid.
     * @return true if parsed data if valid and false otherwise
     */
    public boolean isValid() {
        return color != null && onMs != null && offMs != null;
    }

    @VisibleForTesting
    LedLights(@Nullable Integer color, @Nullable Integer onMs, @Nullable Integer offMs) {
        this.color = color;
        this.onMs = onMs;
        this.offMs = offMs;
    }
}
