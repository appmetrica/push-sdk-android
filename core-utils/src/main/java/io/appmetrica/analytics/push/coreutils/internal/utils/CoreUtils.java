package io.appmetrica.analytics.push.coreutils.internal.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.model.BasePushMessage;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import java.util.Map;

public class CoreUtils {

    private static final String TAG = "[CoreUtils]";

    @Nullable
    public static String getStringFromResources(@NonNull final Context context, @NonNull final String resName) {
        try {
            int resId = getIdentifierForType(context, resName, "string");

            return context.getString(resId);
        } catch (Resources.NotFoundException ignored) {
        }

        return null;
    }

    public static int getIdentifierForType(@NonNull final Context context,
                                           @NonNull final String resName,
                                           @Nullable final String type) {
        return context.getResources().getIdentifier(resName, type, context.getPackageName());
    }

    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(@Nullable String string) {
        return !isEmpty(string);
    }

    @Nullable
    public static String getStringFromMetaData(@NonNull final Context context, @NonNull final String name) {
        final Bundle metaData = getMetaData(context);
        final String value = metaData == null ? null : metaData.getString(name);
        if (TextUtils.isEmpty(value) == false) {
            return value;
        } else {
            return null;
        }
    }

    @Nullable
    public static String getStringOrOtherFromMetaData(@NonNull final Context context, @NonNull final String name) {
        final Bundle metaData = getMetaData(context);
        final Object value = metaData == null ? null :
            metaData.containsKey(name) ? metaData.get(name) : null;
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    public static Bundle getMetaData(@NonNull final Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final String packageName = context.getPackageName();
            final ApplicationInfo info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return info.metaData;
        } catch (Exception ignored) {
        }

        return null;
    }

    @NonNull
    public static Bundle fromMapToBundle(@Nullable final Map<String, String> data) {
        Bundle bundle = new Bundle();
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

    @Nullable
    @Deprecated
    public static String extractRootElement(@NonNull Bundle bundle) {
        return new BasePushMessage(bundle).getRootString();
    }

    public static boolean isNotificationRelatedToSDK(@NonNull Bundle bundle) {
        return new BasePushMessage(bundle).isOwnPush();
    }

    public static <T> void accessSystemServiceSafely(@NonNull final ConsumerWithThrowable<T> tryBlock,
                                                     @Nullable final T systemService,
                                                     @NonNull final String whileWhat,
                                                     @NonNull final String whatIsNull) {
        if (systemService != null) {
            try {
                tryBlock.consume(systemService);
            } catch (Throwable ex) {
                DebugLogger.INSTANCE.error(TAG, ex, "Exception while " + whileWhat);
                TrackersHub.getInstance().reportError("Exception while " + whileWhat, ex);
            }
        } else {
            DebugLogger.INSTANCE.warning(TAG, whatIsNull + " is null.");
            TrackersHub.getInstance().reportEvent(whatIsNull + " is null.");
        }
    }
}
