package io.appmetrica.analytics.push.coreutils.internal.model

import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.logger.internal.PublicLogger
import org.json.JSONObject

open class BasePushMessage(
    bundle: Bundle
) {

    val rootString: String? = bundle.getString(CoreConstants.PushMessage.ROOT_ELEMENT)

    val root: JSONObject? = rootString?.let {
        try {
            JSONObject(it)
        } catch (e: Throwable) {
            PublicLogger.warning("Ignore parse push message exception")
            null
        }
    }

    val isOwnPush = root != null
}
