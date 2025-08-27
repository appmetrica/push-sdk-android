package io.appmetrica.analytics.push.coreutils.internal.commands

import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SystemInfoCommandInfoTest : CommonTest() {

    @Test
    fun toBundleAndFromBundle() {
        val statusChangeTime = 234324L

        val oldInfo = SystemInfoCommandInfo.Builder()
            .withStatusChangeTime(statusChangeTime)
            .build()
        val bundle = oldInfo.toBundle()
        val newInfo = SystemInfoCommandInfo.fromBundle(bundle)

        assertThat(newInfo).usingRecursiveComparison().isEqualTo(oldInfo)
    }

    @Test
    fun toBundleAndFromBundleWithDefaults() {
        val oldInfo = SystemInfoCommandInfo.Builder()
            .build()
        val bundle = oldInfo.toBundle()
        val newInfo = SystemInfoCommandInfo.fromBundle(bundle)

        assertThat(newInfo).usingRecursiveComparison().isEqualTo(oldInfo)
        assertThat(newInfo.statusChangeTime).isNull()
    }
}
