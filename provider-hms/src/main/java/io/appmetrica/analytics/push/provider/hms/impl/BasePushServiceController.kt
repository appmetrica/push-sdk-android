package io.appmetrica.analytics.push.provider.hms.impl

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.provider.api.PushServiceController

open class BasePushServiceController @VisibleForTesting internal constructor(
    val context: Context,
    extractor: IdentifierExtractor
) : PushServiceController {

    val identifier: Identifier by lazy { extractor.extractIdentifier() }
    val exceptionMessage: String by lazy { extractor.exceptionMessage }

    constructor(context: Context) : this(context, DefaultIdentifierFromMetaDataExtractor(context))

    override fun register(): Boolean {
        PLog.d("Register in HMS")
        return if (hmsServicesAvailable()) {
            TokenHolder.getInstance().register(context)
            true
        } else {
            PublicLogger.w("HMS services not available")
            TrackersHub.getInstance().reportEvent("HMS services not available")
            false
        }
    }

    private fun hmsServicesAvailable(): Boolean {
        return try {
            val apiAvailability = HuaweiApiAvailability.getInstance()
            val resultCode = apiAvailability.isHuaweiMobileServicesAvailable(context)
            resultCode == ConnectionResult.SUCCESS
        } catch (ignored: Throwable) {
            false
        }
    }

    override fun getToken(): String? {
        return TokenHolder.getInstance().getToken(identifier)
    }

    override fun getTitle() = CoreConstants.Transport.HMS
}
