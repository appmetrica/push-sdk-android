package io.appmetrica.analytics.push.provider.gcm.impl

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.provider.api.PushServiceController
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions

open class BasePushServiceController @VisibleForTesting internal constructor(
    val context: Context,
    extractor: IdentifierExtractor
) : PushServiceController {

    // https://nda.ya.ru/t/qma58mHp76FPa5
    private val maxTaskExecutionDurationSecondsForGcm = 20L

    val identifier: Identifier by lazy { extractor.extractIdentifier() }
    private var instanceId: InstanceID? = null

    constructor(context: Context) : this(context, DefaultIdentifierFromResourcesExtractor(context))

    override fun register(): Boolean {
        PLog.d("Register in GCM")
        return if (playServicesAvailable()) {
            instanceId = InstanceID.getInstance(context)
            true
        } else {
            PublicLogger.w("Google play services not available")
            TrackersHub.getInstance().reportEvent("Google play services not available")
            false
        }
    }

    private fun playServicesAvailable(): Boolean {
        return try {
            val apiAvailability = GoogleApiAvailability.getInstance()
            val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
            resultCode == ConnectionResult.SUCCESS
        } catch (ignored: Throwable) {
            false
        }
    }

    override fun getToken(): String? {
        return try {
            instanceId?.getToken(identifier.senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE)
        } catch (e: Throwable) {
            PublicLogger.e(e, "Attempt to get push token failed")
            TrackersHub.getInstance().reportError("Attempt to get push token failed", e)
            null
        }
    }

    override fun getTransportId() = CoreConstants.Transport.GCM

    override fun getExecutionRestrictions(): PushServiceExecutionRestrictions =
        object : PushServiceExecutionRestrictions() {
            override fun getMaxTaskExecutionDurationSeconds(): Long = maxTaskExecutionDurationSecondsForGcm
        }
}
