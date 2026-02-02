package io.appmetrica.analytics.push.impl.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;

public class ActivityIntentProvider {

    @Nullable
    public Intent getIntentForActionOrDefaultLaunch(@NonNull final Context context,
                                                    @Nullable final String actionUri) {
        final Intent intent = CoreUtils.isEmpty(actionUri)
            ? getDefaultLaunchIntent(context)
            : getIntentForAction(actionUri);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        return intent;
    }

    @NonNull
    private Intent getIntentForAction(@NonNull final String actionUri) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(actionUri));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private Intent getDefaultLaunchIntent(@NonNull final Context context) {
        Intent intent = null;
        try {
            final Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            if (launchIntent != null && launchIntent.getComponent() != null) {
                final String className = launchIntent.getComponent().getClassName();
                try {
                    final Class<? extends Activity> clazz = (Class<? extends Activity>) Class.forName(className);
                    intent = new Intent(context, clazz);
                } catch (ClassNotFoundException exception) {
                    intent = launchIntent;
                }
                intent.setAction(AppMetricaPush.OPEN_DEFAULT_ACTIVITY_ACTION);
            }
        } catch (Exception ignored) {
        }

        return intent;
    }
}
