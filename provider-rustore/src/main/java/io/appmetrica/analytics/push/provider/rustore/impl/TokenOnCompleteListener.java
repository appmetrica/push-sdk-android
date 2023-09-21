package io.appmetrica.analytics.push.provider.rustore.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.concurrent.CountDownLatch;
import ru.rustore.sdk.core.tasks.OnCompleteListener;

class TokenOnCompleteListener implements OnCompleteListener<String> {

    @NonNull
    private final CountDownLatch countDownLatch;

    @Nullable
    private TokenResult tokenResult = null;

    TokenOnCompleteListener(
        @NonNull final CountDownLatch countDownLatch
    ) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onSuccess(String token) {
        tokenResult = new TokenResult(token, null);
        countDownLatch.countDown();
    }

    @Override
    public void onFailure(@NonNull Throwable throwable) {
        tokenResult = new TokenResult(null, throwable);
        countDownLatch.countDown();
    }

    @Nullable
    TokenResult getTokenResult() {
        return tokenResult;
    }
}
