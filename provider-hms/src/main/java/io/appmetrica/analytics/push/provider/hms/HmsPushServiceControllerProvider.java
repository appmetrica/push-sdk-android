package io.appmetrica.analytics.push.provider.hms;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceControllerProvider;
import io.appmetrica.analytics.push.provider.hms.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.hms.impl.DummyPushServiceController;
import io.appmetrica.analytics.push.provider.hms.impl.Identifier;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link PushServiceControllerProvider} for HMS.
 */
public class HmsPushServiceControllerProvider implements PushServiceControllerProvider {

    @NonNull
    private final List<? extends BasePushServiceController> basePushServiceControllers;

    /**
     * Constructor for {@link HmsPushServiceControllerProvider}.
     *
     * @param context {@link Context} object
     */
    public HmsPushServiceControllerProvider(@NonNull final Context context) {
        this(Collections.singletonList(new BasePushServiceController(context)));
    }

    @VisibleForTesting
    HmsPushServiceControllerProvider(@NonNull final List<BasePushServiceController> basePushServiceControllers) {
        this.basePushServiceControllers = basePushServiceControllers;
    }

    @NonNull
    @Override
    public PushServiceController getPushServiceController() {
        for (final BasePushServiceController controller : basePushServiceControllers) {
            final Identifier identifier = controller.getIdentifier();
            if (identifier.isEmpty() == false) {
                if (identifier.isValid() == true) {
                    return controller;
                } else {
                    Exception exception = new IllegalStateException(controller.getExceptionMessage());
                    PublicLogger.INSTANCE.info("Not found all identifiers");
                    TrackersHub.getInstance().reportError("Not found all identifier", exception);
                }
            }
        }

        PublicLogger.INSTANCE.error(
            new IllegalStateException(CoreConstants.EXCEPTION_MESSAGE_ERROR_ACTIVATE),
            "Failed to activate hms provider"
        );
        return new DummyPushServiceController();
    }
}
