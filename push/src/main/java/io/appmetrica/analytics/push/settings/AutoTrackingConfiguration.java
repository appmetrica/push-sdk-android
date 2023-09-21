package io.appmetrica.analytics.push.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for auto tracking of Push SDK actions.
 */
public class AutoTrackingConfiguration {

    /**
     * Whether to track receive action
     */
    public final boolean trackingReceiveAction;
    /**
     * Whether to track dismiss action
     */
    public final boolean trackingDismissAction;
    /**
     * Whether to track open action
     */
    public final boolean trackingOpenAction;
    /**
     * Whether to track all additional actions
     */
    public final boolean trackingAllAdditionalAction;
    /**
     * Whether to track processed action
     */
    public final boolean trackingProcessedAction;
    /**
     * Set with action IDs with disabled tracking
     */
    @NonNull
    public final Set<String> disabledActionIdSet;

    /**
     * @param actionId action ID
     * @return true if action actions should be tracked and false otherwise
     */
    public boolean isTrackingAdditionalAction(@Nullable final String actionId) {
        return trackingAllAdditionalAction && !disabledActionIdSet.contains(actionId);
    }

    /**
     * @return new {@link Builder}
     */
    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder for {@link AutoTrackingConfiguration}.
     */
    public static class Builder {

        private final boolean trackingReceiveAction = true;
        private final boolean trackingDismissAction = true;
        private boolean trackingOpenAction = true;
        private boolean trackingAllAdditionalAction = true;
        private final boolean trackingProcessedAction = true;
        @NonNull
        private final Set<String> disabledActionIdSet = new HashSet<String>();

        private Builder() {}

        /**
         * Disables tracking for open action.
         *
         * @return same {@link Builder} object
         */
        @NonNull
        public Builder disableTrackingOpenAction() {
            trackingOpenAction = false;
            return this;
        }

        /**
         * Disables tracking for all additional actions.
         *
         * @return same {@link Builder} object
         */
        @NonNull
        public Builder disableTrackingAllAdditionalAction() {
            trackingAllAdditionalAction = false;
            return this;
        }

        /**
         * Disables tracking for additional action.
         *
         * @param actionId action ID
         * @return same {@link Builder} object
         */
        @NonNull
        public Builder disableTrackingAdditionalAction(@NonNull final String actionId) {
            disabledActionIdSet.add(actionId);
            return this;
        }

        /**
         * Builds {@link AutoTrackingConfiguration}.
         *
         * @return new {@link AutoTrackingConfiguration} object
         */
        @NonNull
        public AutoTrackingConfiguration build() {
            return new AutoTrackingConfiguration(this);
        }
    }

    private AutoTrackingConfiguration(@NonNull final Builder builder) {
        trackingReceiveAction = builder.trackingReceiveAction;
        trackingDismissAction = builder.trackingDismissAction;
        trackingOpenAction = builder.trackingOpenAction;
        trackingAllAdditionalAction = builder.trackingAllAdditionalAction;
        trackingProcessedAction = builder.trackingProcessedAction;
        disabledActionIdSet = Collections.unmodifiableSet(builder.disabledActionIdSet);
    }
}
