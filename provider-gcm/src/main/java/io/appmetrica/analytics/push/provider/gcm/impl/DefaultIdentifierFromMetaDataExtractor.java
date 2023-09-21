package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

class DefaultIdentifierFromMetaDataExtractor extends IdentifierExtractor {

    private static final String META_DATA_PROJECT_NUMBER = "ymp_gcm_project_number";

    DefaultIdentifierFromMetaDataExtractor(@NonNull final Context context) {
        super(context);
    }

    @Nullable
    @Override
    @WorkerThread
    String getSenderId() {
        return getSenderIdFromMetaData(getContext(), META_DATA_PROJECT_NUMBER);
    }
}
