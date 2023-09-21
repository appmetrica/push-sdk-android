package io.appmetrica.analytics.push.provider.firebase;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceControllerProvider;
import io.appmetrica.analytics.push.provider.firebase.impl.BasePushServiceController;
import io.appmetrica.analytics.push.provider.firebase.impl.CustomPushServiceController;
import io.appmetrica.analytics.push.provider.firebase.impl.DefaultPushServiceController;
import io.appmetrica.analytics.push.provider.firebase.impl.Identifier;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link PushServiceControllerProvider} for Firebase.
 */
public class FirebasePushServiceControllerProvider implements PushServiceControllerProvider {

    @NonNull
    private final List<? extends BasePushServiceController> basePushServiceControllers;

    /**
     * Constructor for {@link FirebasePushServiceControllerProvider}.
     *
     * @param context {@link Context} object
     */
    public FirebasePushServiceControllerProvider(@NonNull final Context context) {
        this(Arrays.asList(
            new CustomPushServiceController(context),
            new DefaultPushServiceController(context),
            new BasePushServiceController(context)
        ));
    }

    @VisibleForTesting
    FirebasePushServiceControllerProvider(@NonNull final List<BasePushServiceController> basePushServiceControllers) {
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
