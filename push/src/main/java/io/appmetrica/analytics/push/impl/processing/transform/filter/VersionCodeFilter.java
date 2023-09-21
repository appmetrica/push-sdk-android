package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.Locale;

public class VersionCodeFilter implements PushFilter {

    private static final String WRONG_APP_VERSION_CODE = "Wrong app version code";

    private final Context context;

    public VersionCodeFilter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        if (filters == null) {
            return FilterResult.show();
        }

        int versionCode = getVersionCode(context);
        Integer minVersionCode = filters.getMinVersionCode();
        Integer maxVersionCode = filters.getMaxVersionCode();
        if (minVersionCode != null && versionCode < minVersionCode ||
            maxVersionCode != null && versionCode > maxVersionCode) {
            return FilterResult.silence(
                WRONG_APP_VERSION_CODE,
                String.format(
                    Locale.US,
                    "Got app version code [%d], allowed min [%d], allowed max [%d]",
                    versionCode, minVersionCode, maxVersionCode
                )
            );
        }

        return FilterResult.show();
    }

    private static int getVersionCode(@NonNull Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? -1 : packageInfo.versionCode;
    }

    @Nullable
    private static PackageInfo getPackageInfo(@NonNull Context context) {
        PackageInfo packageInfo = null;
        try {
            PackageManager pm = context.getPackageManager();
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (Throwable e) {
            PublicLogger.e(e, e.getMessage());
        }
        return packageInfo;
    }
}
