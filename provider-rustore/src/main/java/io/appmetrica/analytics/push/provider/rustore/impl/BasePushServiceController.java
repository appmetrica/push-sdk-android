package io.appmetrica.analytics.push.provider.rustore.impl;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import ru.rustore.sdk.core.tasks.Task;
import ru.rustore.sdk.pushclient.RuStorePushClient;

public class BasePushServiceController implements PushServiceController {

    private static final long DEFAULT_TOKEN_TIMEOUT = 10;
    private static final TimeUnit DEFAULT_TOKEN_TIMEOUT_TIMEUNIT = TimeUnit.SECONDS;
    private static final Long MAX_TASK_EXECUTION_DURATION_SECONDS = 20L;

    @NonNull
    private final Application application;
    @NonNull
    private final IdentifierExtractor identifierExtractor;

    public BasePushServiceController(@NonNull final Application application) {
        this(application, new DefaultIdentifierFromMetaDataExtractor(application));
    }

    @VisibleForTesting
    BasePushServiceController(
        @NonNull final Application application,
        @NonNull final IdentifierExtractor identifierExtractor
    ) {
        this.application = application;
        this.identifierExtractor = identifierExtractor;
    }

    @Override
    public boolean register() {
        PLog.d("Register in RuStore");
        try {
            RuStorePushClient.INSTANCE.init(application, getIdentifier().projectId);
            return true;
        } catch (Throwable ignore) {
            return false;
        }
    }

    @Nullable
    @Override
    public String getToken() {
        TokenResult tokenResult = getTokenOnce();
        if (tokenResult.isSuccessful()) {
            return tokenResult.token;
        } else {
            PublicLogger.e(tokenResult.exception, "Failed to get token, will retry once");
            tokenResult = getTokenOnce();
            if (tokenResult.isSuccessful()) {
                return tokenResult.token;
            } else {
                PublicLogger.e(tokenResult.exception, "Failed to get token after retry");
                TrackersHub.getInstance().reportError("Attempt to get push token failed", tokenResult.exception);
                return null;
            }
        }
    }

    @NonNull
    private TokenResult getTokenOnce() {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final TokenOnCompleteListener listener = new TokenOnCompleteListener(countDownLatch);
            final Task<String> tokenTask = RuStorePushClient.INSTANCE.getToken();
            tokenTask.addOnSuccessListener(listener);
            tokenTask.addOnFailureListener(listener);
            if (!countDownLatch.await(DEFAULT_TOKEN_TIMEOUT, DEFAULT_TOKEN_TIMEOUT_TIMEUNIT)) {
                throw new TimeoutException("token retrieval timeout");
            }
            TokenResult result = listener.getTokenResult();
            if (result == null) {
                result = new TokenResult(null, new IllegalStateException("token retrieval timeout"));
            }
            return result;
        } catch (Throwable e) {
            return new TokenResult(null, e);
        }
    }

    @NonNull
    @Override
    public String getTransportId() {
        return CoreConstants.Transport.RUSTORE;
    }

    @NonNull
    @Override
    public PushServiceExecutionRestrictions getExecutionRestrictions() {
        return new PushServiceExecutionRestrictions() {
            @Override
            public Long getMaxTaskExecutionDurationSeconds() {
                return MAX_TASK_EXECUTION_DURATION_SECONDS;
            }
        };
    }

    @NonNull
    public Identifier getIdentifier() {
        return identifierExtractor.extractIdentifier();
    }

    @NonNull
    public String getExceptionMessage() {
        return identifierExtractor.getExceptionMessage();
    }
}
