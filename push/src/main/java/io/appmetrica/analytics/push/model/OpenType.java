package io.appmetrica.analytics.push.model;

/**
 * Possible ways to process notification action.
 */
public enum OpenType {
    /**
     * Send broadcast.
     */
    BROADCAST(0),
    /**
     * Open transparent activity.
     */
    TRANSPARENT_ACTIVITY(1),
    /**
     * Open application activity.
     */
    APPLICATION_ACTIVITY(2),
    /**
     * Default value. May differ for different Android API Levels.
     */
    UNKNOWN(-1);

    private final int value;

    OpenType(int value) {
        this.value = value;
    }

    /**
     * Creates from value.
     * @param value open type int value
     * @return parsed enum value
     */
    public static OpenType fromValue(int value) {
        for (OpenType type : OpenType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
