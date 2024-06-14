package io.appmetrica.analytics.push.impl.utils

import io.appmetrica.analytics.push.impl.PreferenceManager
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AppMetricaTrackerEventIdGeneratorTest : CommonTest() {

    private val valueFromPreferences = 10L
    private val scope = "scope"

    private val preferenceManager: PreferenceManager = mock {
        on { getAppMetricaTrackerEventId(scope, -1) } doReturn valueFromPreferences
    }

    private val appMetricaTrackerEventIdGenerator by setUp {
        AppMetricaTrackerEventIdGenerator(preferenceManager, scope)
    }

    @Test
    fun generate() {
        assertThat(appMetricaTrackerEventIdGenerator.generate()).isEqualTo(valueFromPreferences + 1)
        verify(preferenceManager).saveAppMetricaTrackerEventId(scope, valueFromPreferences + 1)
    }
}
