package io.appmetrica.analytics.push.provider.rustore.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import java.util.Locale;

class DefaultIdentifierFromMetaDataExtractor extends IdentifierExtractor {

    private static final String META_DATA_DEFAULT_PROJECT_ID = "ymp_rustore_default_project_id";
    private static final String EXCEPTION_MESSAGE_DEFAULT_ID_FROM_META_DATA_NOT_FOUND = String.format(
        Locale.US,
        EXCEPTION_MESSAGE_EXPECTED_IDENTIFIER_IN_MANIFEST_PATTERN,
        META_DATA_DEFAULT_PROJECT_ID
    );

    DefaultIdentifierFromMetaDataExtractor(
        @NonNull final Context context
    ) {
        super(context, EXCEPTION_MESSAGE_DEFAULT_ID_FROM_META_DATA_NOT_FOUND);
    }

    @Nullable
    @WorkerThread
    @Override
    String getProjectId() {
        return CoreUtils.getStringFromMetaData(getContext(), META_DATA_DEFAULT_PROJECT_ID);
    }
}
