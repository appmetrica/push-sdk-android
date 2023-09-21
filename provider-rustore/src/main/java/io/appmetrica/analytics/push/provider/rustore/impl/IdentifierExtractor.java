package io.appmetrica.analytics.push.provider.rustore.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;

abstract class IdentifierExtractor {

    static final String EXCEPTION_MESSAGE_EXPECTED_IDENTIFIER_IN_MANIFEST_PATTERN =
        "Expected identifier: %s in application block of AndroidManifest.xml. " +
            "See more at " + CoreConstants.LINK_TO_INTEGRATION_PUSH_SDK;

    @NonNull
    private final Context context;
    @NonNull
    private final String exceptionMessage;

    IdentifierExtractor(
        @NonNull final Context context,
        @NonNull final String exceptionMessage
    ) {
        this.context = context;
        this.exceptionMessage = exceptionMessage;
    }

    @NonNull
    Identifier extractIdentifier() {
        return new Identifier(getProjectId());
    }

    @NonNull
    String getExceptionMessage() {
        return exceptionMessage;
    }

    @NonNull
    Context getContext() {
        return context;
    }

    @WorkerThread
    @Nullable
    abstract String getProjectId();
}
