package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;

public class DefaultPushServiceController extends BasePushServiceController {

    public DefaultPushServiceController(@NonNull final Context context) {
        this(context, new DefaultIdentifierFromMetaDataExtractor(context));
    }

    @VisibleForTesting
    DefaultPushServiceController(@NonNull final Context context, @NonNull final IdentifierExtractor extractor) {
        super(context, extractor);
    }

    @NonNull
    @Override
    public FirebaseApp initializeFirebaseApp(@NonNull final FirebaseOptions firebaseOptions) {
        try {
            return FirebaseApp.initializeApp(getContext(), firebaseOptions);
        } catch (Throwable e) {
            PLog.e(e, e.getMessage());
        }

        return FirebaseApp.getInstance();
    }
}
