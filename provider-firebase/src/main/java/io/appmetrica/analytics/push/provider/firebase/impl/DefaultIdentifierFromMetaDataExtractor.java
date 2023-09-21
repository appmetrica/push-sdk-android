package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import java.util.Locale;

class DefaultIdentifierFromMetaDataExtractor extends IdentifierExtractor {

    private static final String META_DATA_DEFAULT_API_KEY = "ymp_firebase_default_api_key";
    private static final String META_DATA_DEFAULT_APP_ID = "ymp_firebase_default_app_id";
    private static final String META_DATA_DEFAULT_SENDER_ID = "ymp_gcm_default_sender_id";
    private static final String META_DATA_DEFAULT_PROJECT_ID = "ymp_firebase_default_project_id";

    private static final String EXCEPTION_MESSAGE_DEFAULT_ID_FROM_META_DATA_NOT_FOUND =
        String.format(Locale.US, EXCEPTION_MESSAGE_EXPECTED_TWO_IDENTIFIERS_IN_MANIFEST_PATTERN,
            META_DATA_DEFAULT_APP_ID, META_DATA_DEFAULT_SENDER_ID);

    DefaultIdentifierFromMetaDataExtractor(@NonNull final Context context) {
        super(context, EXCEPTION_MESSAGE_DEFAULT_ID_FROM_META_DATA_NOT_FOUND);
    }

    @Nullable
    @Override
    @WorkerThread
    String getApiKey() {
        return CoreUtils.getStringFromMetaData(getContext(), META_DATA_DEFAULT_API_KEY);
    }

    @Nullable
    @Override
    @WorkerThread
    String getAppId() {
        return CoreUtils.getStringFromMetaData(getContext(), META_DATA_DEFAULT_APP_ID);
    }

    @Nullable
    @Override
    @WorkerThread
    String getSenderId() {
        return getSenderIdFromMetaData(getContext(), META_DATA_DEFAULT_SENDER_ID);
    }

    @Nullable
    @Override
    String getProjectId() {
        return CoreUtils.getStringFromMetaData(getContext(), META_DATA_DEFAULT_PROJECT_ID);
    }
}
