package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;

abstract class IdentifierExtractor {

    static final String EXCEPTION_MESSAGE_EXPECTED_IDENTIFIER_IN_MANIFEST_PATTERN =
        "Expected identifier: %s in application block of AndroidManifest.xml. " +
            "See more at " + CoreConstants.LINK_TO_INTEGRATION_PUSH_SDK;

    private static final String META_DATA_SENDER_ID_PREFIX = "number";
    private static final String META_DATA_SENDER_ID_SEPARATOR = ":";

    private static final int SENDER_ID_INDEX_PREFIX = 0;
    private static final int SENDER_ID_INDEX_VALUE = 1;
    private static final int SENDER_ID_VALUES_LENGTH = 2;

    @NonNull
    private final Context context;
    @NonNull
    private final String exceptionMessage;

    IdentifierExtractor(@NonNull final Context context,
                        @NonNull final String exceptionMessage) {
        this.context = context;
        this.exceptionMessage = exceptionMessage;
    }

    @Nullable
    String getSenderIdFromMetaData(@NonNull final Context context, @NonNull final String name) {
        String senderId = CoreUtils.getStringFromMetaData(context, name);
        if (TextUtils.isEmpty(senderId) == false) {
            String[] keyValue = senderId.split(META_DATA_SENDER_ID_SEPARATOR);
            if (keyValue.length == SENDER_ID_VALUES_LENGTH
                && META_DATA_SENDER_ID_PREFIX.equals(keyValue[SENDER_ID_INDEX_PREFIX])) {
                return keyValue[SENDER_ID_INDEX_VALUE];
            }
        }

        return null;
    }

    @NonNull
    Identifier extractIdentifier() {
        return new Identifier(getAppId());
    }

    @NonNull
    String getExceptionMessage() {
        return exceptionMessage;
    }

    @NonNull
    Context getContext() {
        return context;
    }

    @Nullable
    @WorkerThread
    abstract String getAppId();

}
