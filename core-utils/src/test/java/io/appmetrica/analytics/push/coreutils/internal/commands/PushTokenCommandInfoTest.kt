package io.appmetrica.analytics.push.coreutils.internal.commands

import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PushTokenCommandInfoTest : CommonTest() {

    @Test
    fun toBundleAndFromBundle() {
        val token = "some token"
        val provider = "some provider"
        val force = true

        val oldInfo = PushTokenCommandInfo.Builder(provider)
            .withToken(token)
            .withForce(force)
            .build()
        val bundle = oldInfo.toBundle()
        val newInfo = PushTokenCommandInfo.fromBundle(bundle)

        assertThat(newInfo).usingRecursiveComparison().isEqualTo(oldInfo)
    }

    @Test
    fun toBundleAndFromBundleIfNoToken() {
        val provider = "some provider"
        val force = true

        val oldInfo = PushTokenCommandInfo.Builder(provider)
            .withForce(force)
            .build()
        val bundle = oldInfo.toBundle()
        val newInfo = PushTokenCommandInfo.fromBundle(bundle)

        assertThat(newInfo).usingRecursiveComparison().isEqualTo(oldInfo)
    }

    @Test
    fun toBundleAndFromBundleWithDefaults() {
        val provider = "some provider"

        val oldInfo = PushTokenCommandInfo.Builder(provider)
            .build()
        val bundle = oldInfo.toBundle()
        val newInfo = PushTokenCommandInfo.fromBundle(bundle)

        assertThat(newInfo).usingRecursiveComparison().isEqualTo(oldInfo)
        assertThat(newInfo.force).isFalse()
    }
}
