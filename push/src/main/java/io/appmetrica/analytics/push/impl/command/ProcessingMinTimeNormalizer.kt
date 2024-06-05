package io.appmetrica.analytics.push.impl.command

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import kotlin.math.min

class ProcessingMinTimeNormalizer {

    private val defaultLimitSeconds = 0L

    fun normalize(context: Context, input: Long, transport: String): Long = input
        .takeIf { it >= 0 }
        ?.let { min(it, transportLimit(context, transport)) }
        ?: defaultLimitSeconds

    private fun transportLimit(context: Context, transport: String): Long =
        AppMetricaPushCore.getInstance(context).pushServiceController
            ?.getExecutionRestrictions(transport)
            ?.maxTaskExecutionDurationSeconds
            ?: defaultLimitSeconds
}
