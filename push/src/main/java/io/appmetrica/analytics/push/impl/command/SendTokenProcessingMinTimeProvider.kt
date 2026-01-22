package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.impl.AppMetricaPushCore

internal class SendTokenProcessingMinTimeProvider : CommandProcessingMinTimeProvider {

    override fun get(context: Context, bundle: Bundle): Long =
        AppMetricaPushCore.getInstance(context).pushServiceController
            ?.getExecutionRestrictions(bundle.getString(CoreConstants.EXTRA_TRANSPORT, CoreConstants.Transport.UNKNOWN))
            ?.maxTaskExecutionDurationSeconds
            ?.let { it / 2 }
            ?: 0L
}
