package io.appmetrica.analytics.push.plugin.adapter.internal;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.google.firebase.messaging.RemoteMessage;
import io.appmetrica.analytics.push.plugin.adapter.impl.Initializer;
import io.appmetrica.analytics.push.provider.firebase.AppMetricaMessagingService;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class AppMetricaPushPluginMessagingService extends AppMetricaMessagingService {

    private Initializer initializer;

    @VisibleForTesting
    Initializer getInitializer() {
        return initializer;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initializer = new Initializer(this);
        getInitializer().initIfNeeded();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        getInitializer().initIfNeeded();
        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        getInitializer().initIfNeeded();
        super.onNewToken(token);
    }
}
