package io.appmetrica.analytics.push.impl

import android.content.Context
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.provider.api.PushServiceController
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions

class PushServiceControllerComposite internal constructor(
    private val context: Context,
    controllers: List<PushServiceController>
) {

    private val controllers: Map<String, PushServiceController> = controllers.associateBy { it.transportId }
    val transportIds: Collection<String> get() = this.controllers.keys

    val tokens: Map<String, String?> get() = this.controllers.mapValues { it.value.token }

    fun register() {
        // We should call register for all controllers
        if (controllers.values.map { it.register() }.any { it }) {
            PushServiceFacade.initToken(context)
        }
    }

    fun getExecutionRestrictions(transport: String): PushServiceExecutionRestrictions? =
        controllers[transport]?.executionRestrictions

    fun shouldSendTokenForProvider(token: String, provider: String) =
        controllers[provider]?.shouldSendToken(token) ?: false
}
