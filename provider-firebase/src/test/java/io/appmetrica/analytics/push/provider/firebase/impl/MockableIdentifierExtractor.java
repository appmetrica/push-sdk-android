package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MockableIdentifierExtractor extends IdentifierExtractor {

    private Identifier mIdentifier;

    MockableIdentifierExtractor(@NonNull final Context context, @NonNull final Identifier identifier) {
        super(context, "");
        mIdentifier = identifier;
    }

    @NonNull
    @Override
    Identifier extractIdentifier() {
        return mIdentifier;
    }

    @Nullable
    @Override
    String getApiKey() {
        return mIdentifier.getApiKey();
    }

    @Nullable
    @Override
    String getAppId() {
        return mIdentifier.getAppId();
    }

    @Nullable
    @Override
    String getSenderId() {
        return mIdentifier.getSenderId();
    }

    @Nullable
    @Override
    String getProjectId() {
        return mIdentifier.getProjectId();
    }
}
