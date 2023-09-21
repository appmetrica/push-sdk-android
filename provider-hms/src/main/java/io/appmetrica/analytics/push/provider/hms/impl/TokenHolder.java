package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.huawei.hms.aaid.HmsInstanceId;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
    Prior to EMUI 10 HmsInstanceId.getToken() does not return token. It initiates a complex process.
    With new token obtained by service as a result.
    This class is a workaround that allows us to handle this situation without SDK architecture redesign.
 */
public class TokenHolder {

    private static final String TAG = "[TokenHolder]";

    private static final class Holder {
        static final TokenHolder INSTANCE = new TokenHolder();
    }

    public static TokenHolder getInstance() {
        return Holder.INSTANCE;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Object monitor = new Object();
    private final CountDownLatch latch = new CountDownLatch(1);
    @Nullable
    private FutureTask<String> getTokenTask = null;
    @Nullable
    private volatile String token = null;

    @Nullable
    private HmsInstanceId instanceId;

    @VisibleForTesting
    TokenHolder() {}

    void register(Context context) {
        instanceId = HmsInstanceId.getInstance(context);
    }

    public void setTokenFromService(@Nullable String token) {
        PLog.d("received token set token %s", token);
        this.token = token;
        latch.countDown();
    }

    @Nullable
    public String getToken(@NonNull final Identifier identifier) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getTokenInternal(identifier);
        } else {
            if (getTokenTask == null) {
                synchronized (monitor) {
                    if (getTokenTask == null) {
                        String receivedToken = getTokenInternal(identifier); //Just initiates token getting process-
                        PLog.d("%s received token from API %s", TAG, receivedToken);
                        //but sometimes you can get token on lower EMUI versions
                        if (TextUtils.isEmpty(receivedToken)) {
                            getTokenTask = new FutureTask<>(new Callable<String>() {
                                @Override
                                public String call() throws Exception {
                                    try {
                                        PLog.d("%s wait for token %s", TAG, token);
                                        latch.await();
                                        PLog.d("%s received token from service %s", TAG, token);
                                        return token;
                                    } catch (Throwable t) {
                                        PLog.e(t, "%s exception while waiting for token", TAG);
                                        return null;
                                    }
                                }
                            });
                        } else {
                            token = receivedToken;
                            return token;
                        }
                    }
                }
            }
            try {
                executor.execute(getTokenTask);
                return getTokenTask.get(10, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                PLog.e(e, "Exception while waiting for token.");
            } catch (InterruptedException e) {
                PLog.e(e, "Exception while waiting for token.");
            } catch (TimeoutException e) {
                PLog.e(e, "Exception while waiting for token.");
            }
            return null;
        }
    }

    @Nullable
    private String getTokenInternal(Identifier identifier) {
        if (instanceId != null) {
            try {
                return instanceId.getToken(identifier.getAppId(), "HCM");
            } catch (Throwable e) {
                PublicLogger.e(e, "Attempt to get push token failed");
                TrackersHub.getInstance().reportError("Attempt to get push token failed", e);
            }
        }
        return null;
    }

}
