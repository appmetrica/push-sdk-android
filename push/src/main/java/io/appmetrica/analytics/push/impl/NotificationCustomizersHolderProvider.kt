package io.appmetrica.analytics.push.impl

import io.appmetrica.analytics.push.notification.NotificationCustomizersHolder

internal object NotificationCustomizersHolderProvider {

    @JvmStatic
    val customizersHolder = NotificationCustomizersHolder()
}
