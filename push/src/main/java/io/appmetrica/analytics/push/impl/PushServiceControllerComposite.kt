package io.appmetrica.analytics.push.impl

import android.content.Context
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.provider.api.PushServiceController
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions

internal class PushServiceControllerComposite internal constructor(
    private val context: Context,
    controllers: List<PushServiceController>
) {

    private val controllers: Map<String, PushServiceController> = controllers.associateBy { it.transportId }
    val transportIds: Set<String> get() = this.controllers.keys

    val tokens: Map<String, String?> get() = this.controllers.mapValues { it.value.token }

    fun register() {
        controllers.values.forEach { controller ->
            if (controller.register()) {
                PushServiceFacade.initToken(context, controller.transportId)
            }
        }
    }

    fun getToken(provider: String): String? = controllers[provider]?.token

    fun getExecutionRestrictions(transport: String): PushServiceExecutionRestrictions? =
        controllers[transport]?.executionRestrictions

    fun shouldSendTokenForProvider(token: String, provider: String) =
        controllers[provider]?.shouldSendToken(token) ?: false
}
