package io.appmetrica.analytics.push.settings;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Interface for custom {@link PushFilter}.
 * Filter can be set via {@link AppMetricaPush#addPushFilter(Context, PushFilter)} method.
 */
public interface PushFilter {

    /**
     * Checks if {@link PushMessage} should be processed.
     *
     * @param pushMessage received {@link PushMessage}
     * @return {@link FilterResult} with status and additional info
     */
    @NonNull
    FilterResult filter(@NonNull PushMessage pushMessage);

    /**
     * Possible statuses of filtration.
     */
    enum FilterResultCode {
        /**
         * {@link PushMessage} should be processed.
         */
        SHOW,
        /**
         * {@link PushMessage} should be ignored.
         */
        SILENCE
    }

    /**
     * Result of {@link PushFilter#filter(PushMessage)} method.
     * Describes status of filtration and its details.
     */
    class FilterResult {

        private static final FilterResult SHOW = new FilterResult(FilterResultCode.SHOW, null, null);

        /**
         * Result code of type {@link FilterResultCode}.
         */
        @NonNull
        public final FilterResultCode filterResultCode;

        /**
         * Category of the reason for ignoring the message
         * if {@link FilterResult#filterResultCode} is {@link FilterResultCode#SILENCE} and null otherwise.
         */
        @Nullable
        public final String category;

        /**
         * Detailed description of the reason for ignoring the message.
         */
        @Nullable
        public final String details;

        /**
         * @return {@link FilterResult} with {@link FilterResultCode#SHOW} status.
         */
        @NonNull
        public static FilterResult show() {
            return SHOW;
        }

        /**
         * @param category category of the reason for ignoring the message
         * @param details detailed description of the reason for ignoring the message
         * @return {@link FilterResult} with {@link FilterResultCode#SILENCE} status.
         */
        @NonNull
        public static FilterResult silence(@Nullable String category, @Nullable String details) {
            return new FilterResult(FilterResultCode.SILENCE, category, details);
        }

        private FilterResult(
            @NonNull FilterResultCode filterResultCode,
            @Nullable String category,
            @Nullable String details
        ) {
            this.filterResultCode = filterResultCode;
            this.category = category;
            this.details = details;
        }

        /**
         * @return true if {@link FilterResult#filterResultCode} is {@link FilterResultCode#SHOW} and false otherwise.
         */
        public boolean isShow() {
            return filterResultCode == FilterResultCode.SHOW;
        }

        /**
         * @return true if {@link FilterResult#filterResultCode} is {@link FilterResultCode#SILENCE}
         * and false otherwise.
         */
        public boolean isSilence() {
            return filterResultCode == FilterResultCode.SILENCE;
        }
    }
}
