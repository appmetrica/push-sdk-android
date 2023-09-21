package io.appmetrica.analytics.push.coreutils.internal.service;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class PushServiceControllerProvider {

    interface PushServiceCommandLauncherCreator {

        @NonNull
        PushServiceCommandLauncher getPushServiceCommandLauncher();
    }

    static class NoServiceCreator implements PushServiceCommandLauncherCreator {

        @NonNull
        final NoServiceController noServiceController;

        public NoServiceCreator(@NonNull Context context) {
            noServiceController = new NoServiceController(context);
        }

        @NonNull
        @Override
        public PushServiceCommandLauncher getPushServiceCommandLauncher() {
            return noServiceController;
        }
    }

    static class PushServiceCreator implements PushServiceCommandLauncherCreator {

        @NonNull
        private final PushServiceController pushServiceController;

        public PushServiceCreator(@NonNull Context context) {
            pushServiceController = new PushServiceController(context);
        }

        @Override
        @NonNull
        public PushServiceCommandLauncher getPushServiceCommandLauncher() {
            return pushServiceController;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    static class PushJobServiceCreator implements PushServiceCommandLauncherCreator {

        @NonNull
        private final PushJobServiceController pushJobServiceController;

        public PushJobServiceCreator(@NonNull Context context) {
            pushJobServiceController = new PushJobServiceController(context);
        }

        @NonNull
        @Override
        public PushServiceCommandLauncher getPushServiceCommandLauncher() {
            return pushJobServiceController;
        }
    }

    @NonNull
    private final PushServiceCommandLauncherCreator pushServiceCommandLauncherCreator;
    @NonNull
    private final PushServiceCommandLauncherCreator noServiceCommandLauncherCreator;

    public PushServiceControllerProvider(@NonNull final Context context) {
        pushServiceCommandLauncherCreator = getPushServiceCommandLauncherCreator(context);
        noServiceCommandLauncherCreator = new NoServiceCreator(context);
    }

    @NonNull
    public PushServiceCommandLauncher getPushServiceCommandLauncher() {
        return getPushServiceCommandLauncher(true);
    }

    @NonNull
    public PushServiceCommandLauncher getPushServiceCommandLauncher(final boolean needService) {
        if (needService) {
            return pushServiceCommandLauncherCreator.getPushServiceCommandLauncher();
        } else {
            return noServiceCommandLauncherCreator.getPushServiceCommandLauncher();
        }
    }

    @NonNull
    private static PushServiceCommandLauncherCreator getPushServiceCommandLauncherCreator(
        @NonNull final Context context
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new PushJobServiceCreator(context);
        } else {
            return new PushServiceCreator(context);
        }
    }
}
