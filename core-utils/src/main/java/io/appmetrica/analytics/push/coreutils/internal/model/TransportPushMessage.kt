package io.appmetrica.analytics.push.coreutils.internal.model

import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.JsonUtils

class TransportPushMessage(
    bundle: Bundle
) : BasePushMessage(bundle) {

    val serviceType by lazy {
        JsonUtils.extractIntegerSafely(root, CoreConstants.PushMessage.SERVICE_TYPE)?.let {
            ServiceType.fromValue(it)
        } ?: ServiceType.UNKNOWN
    }

    val processingMinTime by lazy {
        JsonUtils.extractLongSafely(root, CoreConstants.PushMessage.PROCESSING_MIN_TIME)
    }
}
