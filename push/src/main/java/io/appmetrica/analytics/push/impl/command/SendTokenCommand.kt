package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.impl.AppMetricaPushCore

class SendTokenCommand : Command {

    override fun execute(context: Context, bundle: Bundle) {
        val token = bundle.getString(PushServiceFacade.TOKEN) ?: return

        val transport = bundle.getString(CoreConstants.EXTRA_TRANSPORT, CoreConstants.Transport.UNKNOWN)
        AppMetricaPushCore.getInstance(context).onTokenUpdated(mapOf(transport to token), null)
    }
}
