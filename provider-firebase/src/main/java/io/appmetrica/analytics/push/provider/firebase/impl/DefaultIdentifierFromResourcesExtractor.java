package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import java.util.Locale;

class DefaultIdentifierFromResourcesExtractor extends IdentifierExtractor {

    private static final String DEFAULT_API_KEY_RES_NAME = "google_api_key";
    private static final String DEFAULT_APP_ID_RES_NAME = "google_app_id";
    private static final String DEFAULT_SENDER_ID_RES_NAME = "gcm_defaultSenderId";
    private static final String DEFAULT_PROJECT_ID_RES_NAME = "project_id";

    private static final String EXCEPTION_MESSAGE_EXPECTED_TWO_IDENTIFIERS_IN_RESOURCES_PATTERN =
        "Expected two identifiers: %s and %s in resources. Use gradle plugin com.google.gms.google-services " +
            "in your application build.gradle and add google-services.json in your project. See more at %s";

    private static final String EXCEPTION_MESSAGE_DEFAULT_ID_FROM_RESOURCES_NOT_FOUND =
        String.format(Locale.US, EXCEPTION_MESSAGE_EXPECTED_TWO_IDENTIFIERS_IN_RESOURCES_PATTERN,
            DEFAULT_APP_ID_RES_NAME, DEFAULT_SENDER_ID_RES_NAME, CoreConstants.LINK_TO_INTEGRATION_PUSH_SDK);

    DefaultIdentifierFromResourcesExtractor(@NonNull final Context context) {
        super(context, EXCEPTION_MESSAGE_DEFAULT_ID_FROM_RESOURCES_NOT_FOUND);
    }

    @Nullable
    @Override
    @WorkerThread
    String getApiKey() {
        return CoreUtils.getStringFromResources(getContext(), DEFAULT_API_KEY_RES_NAME);
    }

    @Nullable
    @Override
    @WorkerThread
    String getAppId() {
        return CoreUtils.getStringFromResources(getContext(), DEFAULT_APP_ID_RES_NAME);
    }

    @Nullable
    @Override
    @WorkerThread
    String getSenderId() {
        return CoreUtils.getStringFromResources(getContext(), DEFAULT_SENDER_ID_RES_NAME);
    }

    @Nullable
    @Override
    String getProjectId() {
        return CoreUtils.getStringFromResources(getContext(), DEFAULT_PROJECT_ID_RES_NAME);
    }
}
