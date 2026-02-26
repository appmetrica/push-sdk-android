package io.appmetrica.analytics.push.intent;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Describes {@link NotificationCompat.Action}.
 * Also has data from {@link PushMessage} that is put to {@link PendingIntent}.
 */
public class NotificationActionInfo implements Parcelable {

    /**
     * Transport of current {@link PushMessage}.
     * Possible values listed at {@link CoreConstants.Transport}.
     */
    @NonNull
    public final String transport;
    /**
     * Push ID of current {@link PushMessage}.
     */
    @Nullable
    public final String pushId;
    /**
     * Target action URI of current {@link PushMessage}.
     */
    @Nullable
    public final String targetActionUri;
    /**
     * Payload of current {@link PushMessage}.
     */
    @Nullable
    public final String payload;
    /**
     * Action type of current {@link PushMessage}. Possible values listed at {@link NotificationActionType}.
     */
    @Nullable
    public final NotificationActionType actionType;
    /**
     * Action ID of current {@link PushMessage}.
     */
    @Nullable
    public final String actionId;
    /**
     * Notification tag of current {@link PushMessage}.
     */
    @Nullable
    public final String notificationTag;
    /**
     * Notification ID of current {@link PushMessage}.
     */
    public final int notificationId;
    /**
     * Timeout to hide notification of current {@link PushMessage} in seconds.
     */
    public final long hideAfterSeconds;
    /**
     * Notification channel ID of current {@link PushMessage}.
     */
    @NonNull
    public final String channelId;
    /**
     * Whether to hide quick control panel after clicking on notification of current {@link PushMessage}.
     */
    public final boolean hideQuickControlPanel;
    /**
     * Whether to dismiss notification of current {@link PushMessage} on additional action.
     */
    public final boolean dismissOnAdditionalAction;
    /**
     * Extra bundle of current {@link PushMessage}.
     */
    @Nullable
    public final Bundle extraBundle;
    /**
     * Whether to use explicit intent for notification of current {@link PushMessage}.
     * If true intent will be sent to current application.
     */
    public final boolean explicitIntent;
    /**
     * Whether to do nothing on clicking on notification of current {@link PushMessage}.
     */
    public final boolean doNothing;
    /**
     * Whether to use flag {@link Intent#FLAG_ACTIVITY_NEW_TASK} for notification of current {@link PushMessage}.
     */
    public final boolean useFlagActivityNewTask;

    private NotificationActionInfo(@NonNull final Builder builder) {
        transport = builder.transport;
        pushId = builder.pushId;
        targetActionUri = builder.targetActionUri;
        payload = builder.payload;
        actionType = builder.actionType;
        actionId = builder.actionId;
        notificationTag = builder.notificationTag;
        notificationId = builder.notificationId;
        channelId = builder.channelId;
        hideQuickControlPanel = builder.hideQuickControlPanel;
        dismissOnAdditionalAction = builder.dismissOnAdditionalAction;
        extraBundle = builder.extraBundle;
        explicitIntent = builder.explicitIntent;
        doNothing = builder.doNothing;
        hideAfterSeconds = builder.hideAfterSeconds;
        useFlagActivityNewTask = builder.useFlagActivityNewTask;
    }

    /**
     * Creates the instance of {@link Builder}
     *
     * @param transport transport of current {@link PushMessage}
     * @return builder of {@link NotificationActionInfo}
     */
    @NonNull
    public static Builder newBuilder(@NonNull String transport) {
        return new Builder(transport);
    }

    /**
     * Builds a new {@link NotificationActionInfo} object.
     */
    public static class Builder {

        @NonNull
        private final String transport;
        @Nullable
        private String pushId;
        @Nullable
        private String targetActionUri;
        @Nullable
        private String payload;
        @Nullable
        private NotificationActionType actionType;
        @Nullable
        private String actionId;
        @Nullable
        private String notificationTag;
        private int notificationId = 0;
        private long hideAfterSeconds = 0;
        @NonNull
        private String channelId;
        private boolean hideQuickControlPanel = false;
        private boolean dismissOnAdditionalAction = false;
        @Nullable
        private Bundle extraBundle;
        private boolean explicitIntent;
        private boolean doNothing = false;
        private boolean useFlagActivityNewTask = false;

        private Builder(@NonNull String transport) {
            this.transport = transport;
        }

        /**
         * Sets push ID.
         * @param pushId push ID
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withPushId(@Nullable final String pushId) {
            this.pushId = pushId;
            return this;
        }

        /**
         * Sets target action URI.
         * @param targetActionUri target action URI
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withTargetActionUri(@Nullable final String targetActionUri) {
            this.targetActionUri = targetActionUri;
            return this;
        }

        /**
         * Sets payload.
         * @param payload payload
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withPayload(@Nullable final String payload) {
            this.payload = payload;
            return this;
        }

        /**
         * Sets action type.
         * @param actionType action type
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withActionType(@Nullable final NotificationActionType actionType) {
            this.actionType = actionType;
            return this;
        }

        /**
         * Sets action ID for action tracking.
         * @param actionId action ID for action tracking
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withActionId(@Nullable final String actionId) {
            this.actionId = actionId;
            return this;
        }

        /**
         * Sets notification tag.
         * @param notificationTag notification tag
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withNotificationTag(@Nullable final String notificationTag) {
            this.notificationTag = notificationTag;
            return this;
        }

        /**
         * Sets notification ID.
         * @param notificationId notification ID
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withNotificationId(final int notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        /**
         * Timeout for push notification dismiss itself.
         *
         * @param value timeout in seconds
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withHideAfterSeconds(final long value) {
            this.hideAfterSeconds = value;
            return this;
        }

        /**
         * Sets notification channel ID.
         * @param channelId notification channel ID
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withChannelId(@NonNull final String channelId) {
            this.channelId = channelId;
            return this;
        }

        /**
         * Sets whether to hide quick control panel on clicking on notification.
         * @param hideQuickControlPanel hide quick control panel on clicking on notification
         *                              or additional notification action
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withHideQuickControlPanel(final boolean hideQuickControlPanel) {
            this.hideQuickControlPanel = hideQuickControlPanel;
            return this;
        }

        /**
         * Sets whether to dismiss on additional action.
         * @param autoCancel true if dismiss on additional action and false otherwise
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withDismissOnAdditionalAction(final boolean autoCancel) {
            dismissOnAdditionalAction = autoCancel;
            return this;
        }

        /**
         * Sets extra {@link Bundle}.
         * @param extraBundle extra {@link Bundle}
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withExtraBundle(@Nullable Bundle extraBundle) {
            this.extraBundle = extraBundle == null ? null : new Bundle(extraBundle);
            return this;
        }

        /**
         * Sets whether to use explicit intent.
         * @param explicitIntent true if use explicit intent and false otherwise
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withExplicitIntent(boolean explicitIntent) {
            this.explicitIntent = explicitIntent;
            return this;
        }

        /**
         * Sets whether to do nothing.
         * @param doNothing true if do nothing and false otherwise
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withDoNothing(boolean doNothing) {
            this.doNothing = doNothing;
            return this;
        }

        /**
         * Sets whether to use flag {@link Intent#FLAG_ACTIVITY_NEW_TASK}.
         * @param useFlagActivityNewTask true if use flag {@link Intent#FLAG_ACTIVITY_NEW_TASK} and false otherwise
         * @return the same {@link Builder} instance
         */
        @NonNull
        public Builder withUseFlagActivityNewTask(boolean useFlagActivityNewTask) {
            this.useFlagActivityNewTask = useFlagActivityNewTask;
            return this;
        }

        /**
         * Builds a new {@link NotificationActionInfo} object.
         * @return new {@link NotificationActionInfo} object
         */
        @NonNull
        public NotificationActionInfo build() {
            return new NotificationActionInfo(this);
        }
    }

    private NotificationActionInfo(@NonNull final Parcel in) {
        pushId = in.readString();
        targetActionUri = in.readString();
        payload = in.readString();
        actionType = NotificationActionType.from(in.readString());
        actionId = in.readString();
        notificationTag = in.readString();
        notificationId = in.readInt();
        channelId = in.readString();
        hideQuickControlPanel = readBoolean(in);
        dismissOnAdditionalAction = readBoolean(in);
        extraBundle = in.readBundle(getClass().getClassLoader());
        explicitIntent = readBoolean(in);
        doNothing = readBoolean(in);
        hideAfterSeconds = in.readLong();
        transport = Utils.getOrDefault(in.readString(), CoreConstants.Transport.UNKNOWN);
        useFlagActivityNewTask = readBoolean(in);
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest, final int flags) {
        dest.writeString(pushId);
        dest.writeString(targetActionUri);
        dest.writeString(payload);
        dest.writeString(actionType == null ? null : actionType.getType());
        dest.writeString(actionId);
        dest.writeString(notificationTag);
        dest.writeInt(notificationId);
        dest.writeString(channelId);
        writeBoolean(dest, hideQuickControlPanel);
        writeBoolean(dest, dismissOnAdditionalAction);
        dest.writeBundle(extraBundle);
        writeBoolean(dest, explicitIntent);
        writeBoolean(dest, doNothing);
        dest.writeLong(hideAfterSeconds);
        dest.writeString(transport);
        writeBoolean(dest, useFlagActivityNewTask);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private boolean readBoolean(@NonNull final Parcel in) {
        return in.readInt() == 1;
    }

    private void writeBoolean(@NonNull final Parcel dest, final boolean value) {
        dest.writeInt(value ? 1 : 0);
    }

    /**
     * Creator for {@link Parcelable} interface.
     */
    public static final Creator<NotificationActionInfo> CREATOR =
        new Creator<NotificationActionInfo>() {
            @Override
            public NotificationActionInfo createFromParcel(Parcel in) {
                return new NotificationActionInfo(in);
            }

            @Override
            public NotificationActionInfo[] newArray(int size) {
                return new NotificationActionInfo[size];
            }
        };
}
