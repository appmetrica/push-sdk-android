package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

public class DefaultPushServiceController extends BasePushServiceController {

    public DefaultPushServiceController(@NonNull final Context context) {
        this(context, new DefaultIdentifierFromMetaDataExtractor(context));
    }

    @VisibleForTesting
    DefaultPushServiceController(@NonNull final Context context, @NonNull final IdentifierExtractor extractor) {
        super(context, extractor);
    }
}
