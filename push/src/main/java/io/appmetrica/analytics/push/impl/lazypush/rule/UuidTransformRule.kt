package io.appmetrica.analytics.push.impl.lazypush.rule

import android.content.Context
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule

class UuidTransformRule(
    private val context: Context
) : LazyPushTransformRule {

    override fun getPatternList() = listOf(PATTERN)

    override fun getNewValue(pattern: String): String {
        return try {
            Utils.getOrDefault(AppMetrica.getUuid(context), "")
        } catch (e: Throwable) {
            PublicLogger.e(e, "Cannot get $pattern for AppMetrica version: ${AppMetrica.getLibraryVersion()}")
            ""
        }
    }

    companion object {
        private const val PATTERN = "uuid"
    }
}
