package io.appmetrica.analytics.push.provider.rustore;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceControllerProvider;
import io.appmetrica.analytics.push.provider.rustore.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.rustore.impl.Identifier;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link PushServiceControllerProvider} for RuStore.
 */
public class RuStorePushServiceControllerProvider implements PushServiceControllerProvider {

    @NonNull
    private final List<BasePushServiceController> basePushServiceControllers;

    /**
     * Constructor for {@link RuStorePushServiceControllerProvider}.
     *
     * @param application {@link Application} object
     */
    public RuStorePushServiceControllerProvider(
        @NonNull final Application application
    ) {
        this(Arrays.asList(
            new BasePushServiceController(application)
        ));
    }

    @VisibleForTesting
    RuStorePushServiceControllerProvider(
        @NonNull final List<BasePushServiceController> basePushServiceControllers
    ) {
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
                    throw new IllegalStateException(controller.getExceptionMessage());
                }
            }
        }
        throw new IllegalStateException(CoreConstants.EXCEPTION_MESSAGE_ERROR_ACTIVATE);
    }
}
