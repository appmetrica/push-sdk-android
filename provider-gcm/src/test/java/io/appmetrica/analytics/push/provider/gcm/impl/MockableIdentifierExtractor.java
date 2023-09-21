package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MockableIdentifierExtractor extends IdentifierExtractor {

    private Identifier mIdentifier;

    public MockableIdentifierExtractor(@NonNull final Context context, @NonNull final Identifier identifier) {
        super(context);
        mIdentifier = identifier;
    }

    @NonNull
    @Override
    Identifier extractIdentifier() {
        return mIdentifier;
    }

    @Nullable
    @Override
    String getSenderId() {
        return mIdentifier.getSenderId();
    }
}
