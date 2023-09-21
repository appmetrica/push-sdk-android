package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;

class DefaultIdentifierFromResourcesExtractor extends IdentifierExtractor {

    private static final String DEFAULT_SENDER_ID_RES_NAME = "gcm_defaultSenderId";

    DefaultIdentifierFromResourcesExtractor(@NonNull final Context context) {
        super(context);
    }

    @Nullable
    @Override
    @WorkerThread
    String getSenderId() {
        return CoreUtils.getStringFromResources(getContext(), DEFAULT_SENDER_ID_RES_NAME);
    }
}
