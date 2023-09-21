package io.appmetrica.analytics.push.provider.gcm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceControllerProvider;
import io.appmetrica.analytics.push.provider.gcm.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.gcm.impl.DefaultPushServiceController;
import io.appmetrica.analytics.push.provider.gcm.impl.Identifier;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link PushServiceControllerProvider} for GCM.
 */
public class GcmPushServiceControllerProvider implements PushServiceControllerProvider {

    @NonNull
    private final List<? extends BasePushServiceController> basePushServiceControllers;

    /**
     * Constructor for {@link GcmPushServiceControllerProvider}.
     *
     * @param context {@link Context} object
     */
    public GcmPushServiceControllerProvider(@NonNull final Context context) {
        this(Arrays.asList(
            new DefaultPushServiceController(context),
            new BasePushServiceController(context)
        ));
    }

    @VisibleForTesting
    GcmPushServiceControllerProvider(@NonNull final List<BasePushServiceController> basePushServiceControllers) {
        this.basePushServiceControllers = basePushServiceControllers;
    }

    @NonNull
    @Override
    public PushServiceController getPushServiceController() {
        for (final BasePushServiceController controller : basePushServiceControllers) {
            final Identifier identifier = controller.getIdentifier();
            if (identifier.isValid()) {
                return controller;
            }
        }

        throw new IllegalStateException(CoreConstants.EXCEPTION_MESSAGE_ERROR_ACTIVATE);
    }
}
