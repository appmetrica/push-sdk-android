package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import io.appmetrica.analytics.push.settings.PushFilter;

class PassportUidFilter implements PushFilter {

    private static final String NOT_FOUND_PROVIDER = "Not found passport uid provider";
    private static final String NO_CURRENT_ACCOUNT = "No current account";
    private static final String WRONG_ACCOUNT = "Wrong account";

    @NonNull
    private final AppMetricaPushCore appMetricaPushCore;

    public PassportUidFilter(@NonNull AppMetricaPushCore appMetricaPushCore) {
        this.appMetricaPushCore = appMetricaPushCore;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        String passportUid = filters == null ? null : filters.getPassportUid();
        if (TextUtils.isEmpty(passportUid)) {
            return FilterResult.show();
        }

        PassportUidProvider passportUidProvider = appMetricaPushCore.getPassportUidProvider();
        if (passportUidProvider == null) {
            return FilterResult.silence(NOT_FOUND_PROVIDER, null);
        }

        String curPassportUid = passportUidProvider.getUid();
        if (TextUtils.isEmpty(curPassportUid)) {
            return FilterResult.silence(NO_CURRENT_ACCOUNT, null);
        }

        if (!TextUtils.equals(passportUid, curPassportUid)) {
            return FilterResult.silence(
                WRONG_ACCOUNT,
                String.format("Got account uid [%s], allowed [%s]", curPassportUid, passportUid)
            );
        }

        return FilterResult.show();
    }
}
