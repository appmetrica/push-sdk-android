package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;

public class CustomPushServiceController extends BasePushServiceController {

    private static final String TAG = "[CustomPushServiceController]";

    @VisibleForTesting
    static final String CUSTOM_FIREBASE_APP_NAME = "METRICA_PUSH";

    public CustomPushServiceController(@NonNull final Context context) {
        this(context, new CustomIdentifierFromMetaDataExtractor(context));
    }

    @VisibleForTesting
    CustomPushServiceController(@NonNull final Context context, @NonNull final IdentifierExtractor extractor) {
        super(context, extractor);
    }

    @NonNull
    @Override
    public FirebaseApp initializeFirebaseApp(@NonNull final FirebaseOptions firebaseOptions) {
        try {
            return FirebaseApp.initializeApp(getContext(), firebaseOptions, CUSTOM_FIREBASE_APP_NAME);
        } catch (Throwable e) {
            DebugLogger.INSTANCE.error(TAG, e, e.getMessage());
        }

        return FirebaseApp.getInstance(CUSTOM_FIREBASE_APP_NAME);
    }
}
