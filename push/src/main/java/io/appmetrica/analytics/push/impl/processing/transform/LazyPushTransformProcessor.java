package io.appmetrica.analytics.push.impl.processing.transform;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.lazypush.DefaultLazyPushTransformRuleProviders;
import io.appmetrica.analytics.push.impl.processing.transform.filter.FilterFacade;
import io.appmetrica.analytics.push.impl.processing.transform.lazypush.LazyPushResponse;
import io.appmetrica.analytics.push.impl.utils.StringTransform;
import io.appmetrica.analytics.push.impl.utils.network.HttpConnection;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import io.appmetrica.analytics.push.model.LazyPushRequestInfo;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LazyPushTransformProcessor extends TransformProcessor {

    private static final long[] DEFAULT_RETRY_STRATEGY_SECONDS = new long[]{1, 1, 1};

    private static final String BACKEND_NOT_AVAILABLE = "Backend not available";
    private static final String FILTERED_BY_BACKEND = "Filtered by backend";
    private static final String UNKNOWN_RESPONSE_BY_BACKEND = "Unknown response by backend";
    private static final String FAILED_MERGE_PUSH_MESSAGES = "Failed merge push messages";

    @NonNull
    private final Context context;

    public LazyPushTransformProcessor(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TransformResult transform(@NonNull PushMessage pushMessage) {
        LazyPushRequestInfo lazyPushRequestInfo = pushMessage.getLazyPushRequestInfo();
        String url = lazyPushRequestInfo == null ? null : lazyPushRequestInfo.getUrl();
        if (CoreUtils.isEmpty(url)) {
            return success(pushMessage);
        }

        checkFilters(pushMessage);

        StringTransform stringTransform = new DefaultLazyPushTransformRuleProviders()
            .getAsStringTransform(context, pushMessage);
        String realUrl = stringTransform.transform(url);
        Map<String, String> headers = transformHeaders(lazyPushRequestInfo.getHeaders(), stringTransform);

        try {
            return handleResponse(request(realUrl, headers, getRetryStrategySeconds(lazyPushRequestInfo)), pushMessage);
        } catch (Throwable e) {
            if (isUseCurPushAsFallback(lazyPushRequestInfo)) {
                return success(pushMessage);
            }
            return failure(pushMessage, BACKEND_NOT_AVAILABLE, e.getMessage());
        }
    }

    private long[] getRetryStrategySeconds(@NonNull LazyPushRequestInfo requestInfo) {
        long[] retryStrategy = requestInfo.getRetryStrategySeconds();
        return retryStrategy != null ? retryStrategy : DEFAULT_RETRY_STRATEGY_SECONDS;
    }

    private void checkFilters(@NonNull PushMessage pushMessage) {
        FilterFacade facade = AppMetricaPushCore.getInstance(context).getPreLazyFilterFacade();
        PushFilter.FilterResult result = facade.filter(pushMessage);
        if (result.isSilence()) {
            throw new TransformFailureException(result.category, result.details);
        }
    }

    @NonNull
    private TransformResult handleResponse(@NonNull LazyPushResponse response, @NonNull PushMessage pushMessage) {
        if (!response.isCorrect()) {
            return failure(pushMessage, UNKNOWN_RESPONSE_BY_BACKEND, response.getErrorParseMessage());
        } else if (response.isIgnored()) {
            return failure(pushMessage, FILTERED_BY_BACKEND, response.getIgnoreDetails());
        } else {
            try {
                return success(pushMessage.append(response.getPushMessageJson()));
            } catch (Throwable e) {
                return failure(pushMessage, FAILED_MERGE_PUSH_MESSAGES, e.getMessage());
            }
        }
    }

    private static boolean isUseCurPushAsFallback(@NonNull LazyPushRequestInfo lazyPushRequestInfo) {
        return Boolean.TRUE.equals(lazyPushRequestInfo.getUseCurPushAsFallback());
    }

    @NonNull
    private LazyPushResponse request(
        @NonNull String url,
        @NonNull Map<String, String> headers,
        @NonNull long[] retryStrategySeconds
    ) throws IOException {
        PublicLogger.INSTANCE.info(
            "Request lazy push from %s with retry policy %s",
            url,
            Arrays.toString(retryStrategySeconds)
        );
        int retryCount = 0;
        while (true) {
            try {
                PublicLogger.INSTANCE.info("Request #%d for %s", retryCount, url);
                byte[] response = new HttpConnection(url, headers).get();
                PublicLogger.INSTANCE.info(
                    "Response for %s: '%s'",
                    url,
                    new String(response, Charset.forName("UTF-8"))
                );
                return new LazyPushResponse(response);
            } catch (IOException e) {
                if (retryCount >= retryStrategySeconds.length) {
                    throw e;
                }
                try {
                    long waitSeconds = retryStrategySeconds[retryCount++];
                    PublicLogger.INSTANCE.info("Wait %d seconds before next request for %s", waitSeconds, url);
                    Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
                } catch (InterruptedException ex) {
                    PublicLogger.INSTANCE.error(ex, ex.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @NonNull
    private static Map<String, String> transformHeaders(
        @Nullable Map<String, String> headers,
        @NonNull StringTransform stringTransform
    ) {
        Map<String, String> realHeaders = new HashMap<String, String>();
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                realHeaders.put(header.getKey(), stringTransform.transform(header.getValue()));
            }
        }
        return realHeaders;
    }
}
