package io.appmetrica.analytics.push.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Possibles action types.
 */
public enum NotificationActionType {
    /**
     * Remove notification by user action or by code.
     */
    CLEAR("clear"),
    /**
     * Click on notification.
     */
    CLICK("click"),
    /**
     * Click on button of notification.
     */
    ADDITIONAL_ACTION("additional"),
    /**
     * Inline action of notification. Like messaging back for messaging applications.
     */
    INLINE_ACTION("inline");

    @NonNull
    private final String type;

    // do not use annotations since Proguard incorrectly transforms it for enums and Jetifier cannot read it
    NotificationActionType(final String type) {
        this.type = type;
    }

    /**
     * @return {@link String} value of action type
     */
    @NonNull
    public String getType() {
        return type;
    }

    /**
     * @param type {@link String} value of action type
     * @return parsed {@link NotificationActionType} if it is present in enum or null otherwise
     */
    @Nullable
    public static NotificationActionType from(@Nullable String type) {
        for (NotificationActionType notificationActionType : NotificationActionType.values()) {
            if (notificationActionType.type.equals(type)) {
                return notificationActionType;
            }
        }
        return null;
    }
}
