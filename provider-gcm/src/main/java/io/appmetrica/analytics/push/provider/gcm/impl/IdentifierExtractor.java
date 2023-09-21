package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;

abstract class IdentifierExtractor {

    private static final String META_DATA_SENDER_ID_PREFIX = "number";
    private static final String META_DATA_SENDER_ID_SEPARATOR = ":";

    private static final int SENDER_ID_INDEX_PREFIX = 0;
    private static final int SENDER_ID_INDEX_VALUE = 1;
    private static final int SENDER_ID_VALUES_LENGTH = 2;

    @NonNull
    private final Context context;

    IdentifierExtractor(@NonNull final Context context) {
        this.context = context;
    }

    @Nullable
    String getSenderIdFromMetaData(@NonNull final Context context, @NonNull final String name) {
        String senderId = CoreUtils.getStringFromMetaData(context, name);
        if (CoreUtils.isEmpty(senderId) == false) {
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
        return new Identifier(getSenderId());
    }

    @NonNull
    Context getContext() {
        return context;
    }

    @Nullable
    @WorkerThread
    abstract String getSenderId();
}
