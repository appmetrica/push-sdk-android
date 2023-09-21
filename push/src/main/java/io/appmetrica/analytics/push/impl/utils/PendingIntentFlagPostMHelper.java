package io.appmetrica.analytics.push.impl.utils;

import android.app.PendingIntent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline;

@DoNotInline
@RequiresApi(Build.VERSION_CODES.M)
class PendingIntentFlagPostMHelper {

    static int getPendingIntentFlag(int base, boolean needMutableFlag) {
        return base | PendingIntent.FLAG_IMMUTABLE;
    }
}
