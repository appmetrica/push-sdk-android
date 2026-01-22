package io.appmetrica.analytics.push.provider.firebase.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.logger.internal.DebugLogger
import io.appmetrica.analytics.push.logger.internal.PublicLogger
import io.appmetrica.analytics.push.provider.api.PushServiceController
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private const val DEFAULT_TOKEN_TIMEOUT = 10L
private val DEFAULT_TOKEN_TIMEOUT_TIMEUNIT = TimeUnit.SECONDS

internal open class BasePushServiceController @VisibleForTesting internal constructor(
    val context: Context,
    extractor: IdentifierExtractor
) : PushServiceController {

    private val tag = "[BasePushServiceController]"

    // https://nda.ya.ru/t/qma58mHp76FPa5
    private val maxTaskExecutionDurationSecondsForFirebase = 20L

    val identifier: Identifier by lazy { extractor.extractIdentifier() }
    val exceptionMessage: String by lazy { extractor.exceptionMessage }
    private var firebaseMessaging: FirebaseMessaging? = null

    constructor(context: Context) : this(context, DefaultIdentifierFromResourcesExtractor(context))

    override fun register(): Boolean {
        DebugLogger.info(tag, "Register in Firebase")
        return if (playServicesAvailable()) {
            val firebaseApp = initializeFirebaseApp(identifier.toFirebaseOptions())
            // FirebaseMessaging.getInstance(FirebaseApp) should be public due to documentation but it is package private
            // Found workaround at https://stackoverflow.com/questions/62253691/firebasemessaging-getinstancefirebaseapp-for-secondary-app-supposed-to-be-publ
            firebaseMessaging = firebaseApp.get(FirebaseMessaging::class.java)
            true
        } else {
            PublicLogger.warning("Google play services not available")
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

    open fun initializeFirebaseApp(firebaseOptions: FirebaseOptions): FirebaseApp {
        try {
            FirebaseApp.initializeApp(context, firebaseOptions)
        } catch (e: Throwable) {
            DebugLogger.error(tag, e, e.message)
        }
        return FirebaseApp.getInstance()
    }

    @SuppressLint("MissingFirebaseInstanceTokenRefresh")
    override fun getToken(): String? {
        val firebaseMessaging = firebaseMessaging
        if (firebaseMessaging == null) {
            TrackersHub.getInstance().reportEvent("Attempt to get push token failed since firebaseMessaging is null")
            return null
        }
        var tokenResult = getToken(firebaseMessaging)
        return if (tokenResult.isSuccess) {
            tokenResult.token
        } else {
            PublicLogger.error(tokenResult.exception, "Failed to get token, will retry once")
            tokenResult = getToken(firebaseMessaging)
            if (tokenResult.isSuccess) {
                tokenResult.token
            } else {
                PublicLogger.error(tokenResult.exception, "Failed to get token after retry")
                TrackersHub.getInstance().reportError("Attempt to get push token failed", tokenResult.exception)
                null
            }
        }
    }

    private fun getToken(firebaseMessaging: FirebaseMessaging): TokenResult {
        return try {
            val countDownLatch = CountDownLatch(1)
            val getTokenTask = firebaseMessaging.token
            getTokenTask.addOnCompleteListener { countDownLatch.countDown() }
            if (!countDownLatch.await(DEFAULT_TOKEN_TIMEOUT, DEFAULT_TOKEN_TIMEOUT_TIMEUNIT)) {
                throw TimeoutException("token retrieval timeout")
            }
            if (getTokenTask.isSuccessful) {
                TokenResult(token = getTokenTask.result)
            } else {
                TokenResult(exception = getTokenTask.exception)
            }
        } catch (e: Throwable) {
            TokenResult(exception = e)
        }
    }

    override fun getTransportId(): String = CoreConstants.Transport.FIREBASE

    override fun getExecutionRestrictions(): PushServiceExecutionRestrictions =
        object : PushServiceExecutionRestrictions() {
            override fun getMaxTaskExecutionDurationSeconds(): Long = maxTaskExecutionDurationSecondsForFirebase
        }

    private class TokenResult(
        val token: String? = null,
        val exception: Throwable? = null
    ) {

        val isSuccess by lazy { exception == null && token != null }
    }

    override fun shouldSendToken(token: String): Boolean = token == getToken()
}
