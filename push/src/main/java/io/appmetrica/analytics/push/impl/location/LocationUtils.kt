package io.appmetrica.analytics.push.impl.location

import android.location.Location
import android.os.Build
import io.appmetrica.analytics.push.impl.utils.Utils

object LocationUtils {

    @JvmStatic
    fun getRecency(location: Location): Long {
        return if (Utils.isApiAchived(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            LocationUtilsPostJellyBean.getRecency(location)
        } else {
            LocationUtilsPreJellyBean.getRecency(location)
        }
    }
}
