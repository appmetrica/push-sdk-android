package io.appmetrica.analytics.push.model;

/**
 * Possible additional action types for {@link AdditionalAction#getType()}.
 */
public enum AdditionalActionType {
    /**
     * Open URI in browser.
     */
    OPEN_URI(0),
    /**
     * Open application URI.
     */
    OPEN_APP_URI(1),
    /**
     * Do nothing.
     */
    DO_NOTHING(2),
    /**
     * Inline action.
     */
    INLINE(3),
    /**
     * Unknown type.
     */
    UNKNOWN(-1);

    private final int value;

    AdditionalActionType(int value) {
        this.value = value;
    }

    /**
     * @param value int value of {@link AdditionalActionType}
     * @return {@link AdditionalActionType} if it is present or {@link AdditionalActionType#UNKNOWN} otherwise
     */
    public static AdditionalActionType fromValue(int value) {
        for (AdditionalActionType additionalActionType : AdditionalActionType.values()) {
            if (additionalActionType.value == value) {
                return additionalActionType;
            }
        }
        return UNKNOWN;
    }
}
