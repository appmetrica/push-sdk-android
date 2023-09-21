package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline;

@DoNotInline
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class UtilsPostLollipop {

    @Nullable
    public static Drawable getDrawable(@NonNull final Context context, @NonNull final Integer resId) {
        Drawable drawable = null;
        try {
            drawable = context.getResources().getDrawable(resId, null);
        } catch (Resources.NotFoundException ignored) {
        }
        return drawable;
    }
}
