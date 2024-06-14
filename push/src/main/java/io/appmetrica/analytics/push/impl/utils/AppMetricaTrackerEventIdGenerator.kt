package io.appmetrica.analytics.push.impl.utils

import io.appmetrica.analytics.push.impl.PreferenceManager

internal class AppMetricaTrackerEventIdGenerator(
    private val preferenceManager: PreferenceManager,
    private val scope: String,
) {

    @Synchronized
    fun generate(): Long {
        val actualId = preferenceManager.getAppMetricaTrackerEventId(scope, -1) + 1
        preferenceManager.saveAppMetricaTrackerEventId(scope, actualId)
        return actualId
    }
}
