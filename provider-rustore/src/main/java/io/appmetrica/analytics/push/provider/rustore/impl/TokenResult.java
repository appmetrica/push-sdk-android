package io.appmetrica.analytics.push.provider.rustore.impl;

import androidx.annotation.Nullable;

class TokenResult {

    @Nullable
    final String token;
    @Nullable
    final Throwable exception;

    TokenResult(
        @Nullable final String token,
        @Nullable final Throwable exception
    ) {
        this.token = token;
        this.exception = exception;
    }

    boolean isSuccessful() {
        return exception == null && token != null;
    }
}
