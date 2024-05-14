package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.model.BasePushMessage;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.impl.Constants;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONObject;

public class Utils {

    @Nullable
    public static Integer wrapSoundResId(@NonNull final Context context, @Nullable final String input) {
        Integer resId = null;
        if (input != null) {
            try {
                resId = Integer.parseInt(input);
                if (getRaw(context, resId) == null) {
                    resId = null;
                }
            } catch (NumberFormatException ignored) {
                resId = extractRawResIdByName(context, input);
            }
        }
        return resId == null || resId == 0 ? null : resId;
    }

    @Nullable
    public static Integer wrapResId(@NonNull final Context context, @Nullable final String input) {
        Integer resId = null;
        if (input != null) {
            try {
                resId = Integer.parseInt(input);
                if (getDrawable(context, resId) == null) {
                    resId = null;
                }
            } catch (NumberFormatException ignored) {
                resId = extractDrawableResIdByName(context, input);
                if (resId == 0) {
                    resId = extractMipmapResIdByName(context, input);
                }
            }
        }
        return resId == null || resId == 0 ? null : resId;
    }

    @NonNull
    public static <T> T getOrDefault(@Nullable T value, @NonNull T def) {
        return value == null ? def : value;
    }

    @Nullable
    private static Drawable getDrawable(@NonNull final Context context, @NonNull final Integer resId) {
        Drawable drawable = null;
        try {
            if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.LOLLIPOP)) {
                drawable = UtilsPostLollipop.getDrawable(context, resId);
            } else {
                drawable = context.getResources().getDrawable(resId);
            }
        } catch (Resources.NotFoundException ignored) {
        }
        return drawable;
    }

    @Nullable
    private static InputStream getRaw(@NonNull final Context context, @NonNull final Integer resId) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(resId);
        } catch (Resources.NotFoundException ignored) {
        }
        return inputStream;
    }

    @NonNull
    public static Integer extractRawResIdByName(@NonNull final Context context, @NonNull final String resName) {
        return CoreUtils.getIdentifierForType(context, resName, "raw");
    }

    @NonNull
    public static Integer extractDrawableResIdByName(@NonNull final Context context, @NonNull final String resName) {
        return CoreUtils.getIdentifierForType(context, resName, "drawable");
    }

    @NonNull
    public static Integer extractMipmapResIdByName(@NonNull final Context context, @NonNull final String resName) {
        return CoreUtils.getIdentifierForType(context, resName, "mipmap");
    }

    public static float getNotificationLargeIconWidth(@NonNull final Context context) {
        return getNotificationLargeIconWidthFromResources(context);
    }

    public static float getNotificationLargeIconHeight(@NonNull final Context context) {
        return getNotificationLargeIconHeightFromResources(context);
    }

    private static float getNotificationLargeIconWidthFromResources(@NonNull final Context context) {
        return context.getResources().getDimension(android.R.dimen.notification_large_icon_width);
    }

    private static float getNotificationLargeIconHeightFromResources(@NonNull final Context context) {
        return context.getResources().getDimension(android.R.dimen.notification_large_icon_height);
    }

    @Nullable
    public static Integer getIntegerFromMetaData(@NonNull final Context context, @NonNull final String name) {
        final Bundle metaData = CoreUtils.getMetaData(context);
        if (metaData == null) {
            return null;
        }
        return metaData.containsKey(name) ? metaData.getInt(name) : null;
    }

    public static boolean isClassExists(@NonNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignore) {
        }
        return false;
    }

    public static void closeSilently(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                PLog.e(e.getMessage(), e);
            }
        }
    }

    @Nullable
    public static Bitmap drawableToBitmap(@Nullable Drawable drawable, float width, float height, float density) {
        if (drawable == null) {
            return null;
        }

        Rect drawableBounds = drawable.getBounds();

        float widthPx = width * density;
        float heightPx = height * density;

        if (heightPx <= 0 || widthPx <= 0) {

            int intrWidth = drawable.getIntrinsicWidth();
            int intrHeight = drawable.getIntrinsicHeight();

            //falling back to bounds if needed
            intrWidth = intrWidth > 0 ? intrWidth : drawableBounds.width();
            intrHeight = intrHeight > 0 ? intrHeight : drawableBounds.height();

            if (heightPx <= 0 && widthPx <= 0) {
                widthPx = intrWidth;
                heightPx = intrHeight;
            } else if (heightPx <= 0 && widthPx > 0 && intrWidth > 0) {
                heightPx = widthPx * intrHeight / intrWidth;
            } else if (heightPx > 0 && widthPx <= 0 && intrHeight > 0) {
                widthPx = heightPx * intrWidth / intrHeight;
            }
        }

        if (widthPx <= 0 || heightPx <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap((int) widthPx, (int) heightPx, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        drawable.setBounds(drawableBounds);

        return bitmap;
    }

    @Nullable
    public static Bitmap getBitmapFromResources(
        @NonNull Context context,
        @DrawableRes int id,
        float width,
        float height
    ) {
        float density = context.getResources().getDisplayMetrics().density;
        return drawableToBitmap(getDrawable(context, id), width, height, density);
    }

    public static void copy(@NonNull InputStream in, @NonNull OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static boolean equals(@Nullable Object obj1, @Nullable Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        return obj1 != null && obj1.equals(obj2);
    }

    @Nullable
    public static String extractPushId(@NonNull Bundle bundle) {
        try {
            final BasePushMessage push = new BasePushMessage(bundle);
            final JSONObject root = push.getRoot();
            if (root != null && root.has(Constants.PushMessage.NOTIFICATION_ID)) {
                return root.getString(Constants.PushMessage.NOTIFICATION_ID);
            }
        } catch (Throwable ignore) {
        }
        return null;
    }

    @NonNull
    public static Spanned wrapHtml(@Nullable final String value) {
        return Html.fromHtml(value);
    }
}
