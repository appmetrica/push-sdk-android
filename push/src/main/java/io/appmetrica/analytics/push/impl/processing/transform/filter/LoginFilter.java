package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import io.appmetrica.analytics.push.settings.PushFilter;

public class LoginFilter implements PushFilter {

    private static final String NOT_FOUND_PROVIDER = "Not found passport uid provider";
    private static final String NO_CURRENT_ACCOUNT = "No current account";
    private static final String HAS_CURRENT_ACCOUNT = "Has current account";
    private static final String DO_NOT_SHOW = "Filter type is set to 'do not show to anyone'";

    private static final int SHOW_TO_LOGGED_IN = 1;
    private static final int SHOW_TO_NOT_LOGGED_IN = 2;
    private static final int DEFAULT_LOGIN_FILTER_TYPE = 3;

    @NonNull
    private final AppMetricaPushCore appMetricaPushCore;

    public LoginFilter(@NonNull AppMetricaPushCore appMetricaPushCore) {
        this.appMetricaPushCore = appMetricaPushCore;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        final Filters filters = pushMessage.getFilters();
        final int loginFilterType = getLoginFilterType(filters);
        if ((loginFilterType & SHOW_TO_LOGGED_IN) != 0
            && (loginFilterType & SHOW_TO_NOT_LOGGED_IN) != 0) {
            return FilterResult.show();
        }

        final PassportUidProvider passportUidProvider = appMetricaPushCore.getPassportUidProvider();
        if (passportUidProvider == null) {
            return FilterResult.silence(NOT_FOUND_PROVIDER, null);
        }

        final String curPassportUid = passportUidProvider.getUid();
        if ((loginFilterType & SHOW_TO_LOGGED_IN) != 0) {
            if (TextUtils.isEmpty(curPassportUid)) {
                return FilterResult.silence(NO_CURRENT_ACCOUNT, null);
            } else {
                return FilterResult.show();
            }
        }
        if ((loginFilterType & SHOW_TO_NOT_LOGGED_IN) != 0) {
            if (TextUtils.isEmpty(curPassportUid)) {
                return FilterResult.show();
            } else {
                return FilterResult.silence(HAS_CURRENT_ACCOUNT, null);
            }
        }

        return FilterResult.silence(DO_NOT_SHOW, null);
    }

    private static int getLoginFilterType(@Nullable Filters filters) {
        final Integer loginFilterType = filters == null ? null : filters.getLoginFilterType();
        return loginFilterType != null ? loginFilterType : DEFAULT_LOGIN_FILTER_TYPE;
    }
}
