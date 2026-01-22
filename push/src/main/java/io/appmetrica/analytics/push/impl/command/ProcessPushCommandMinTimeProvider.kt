package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import kotlin.math.min

internal class ProcessPushCommandMinTimeProvider : CommandProcessingMinTimeProvider {

    private val defaultLimitSeconds = 0L

    override fun get(context: Context, bundle: Bundle): Long = normalize(
        context,
        bundle.getLong(CoreConstants.MIN_PROCESSING_DELAY, -1),
        bundle.getString(CoreConstants.EXTRA_TRANSPORT, CoreConstants.Transport.UNKNOWN)
    )

    private fun normalize(context: Context, input: Long, transport: String): Long = input
        .takeIf { it >= 0 }
        ?.let { min(it, transportLimit(context, transport)) }
        ?: defaultLimitSeconds

    private fun transportLimit(context: Context, transport: String): Long =
        AppMetricaPushCore.getInstance(context).pushServiceController
            ?.getExecutionRestrictions(transport)
            ?.maxTaskExecutionDurationSeconds
            ?: defaultLimitSeconds
}
