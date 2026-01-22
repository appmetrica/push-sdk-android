package io.appmetrica.analytics.push.impl.lazypush.rule

import android.content.Context
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.logger.internal.PublicLogger

internal class DeviceIdTransformRule(
    private val context: Context
) : LazyPushTransformRule {

    override fun getPatternList() = listOf(PATTERN)

    override fun getNewValue(pattern: String): String {
        return try {
            Utils.getOrDefault(AppMetrica.getDeviceId(context), "")
        } catch (e: Throwable) {
            PublicLogger.error(e, "Cannot get $pattern for AppMetrica version: ${AppMetrica.getLibraryVersion()}")
            ""
        }
    }

    companion object {
        private const val PATTERN = "deviceId"
    }
}
