package io.appmetrica.analytics.push.impl.utils;

import android.os.Build;

public class PendingIntentFlagHelper {

    public static int getPendingIntentFlag(int base, boolean needMutableFlag) {
        if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.S)) {
            return PendingIntentFlagPostSHelper.getPendingIntentFlag(base, needMutableFlag);
        }
        if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.M)) {
            return PendingIntentFlagPostMHelper.getPendingIntentFlag(base, needMutableFlag);
        }
        return base;
    }
}
