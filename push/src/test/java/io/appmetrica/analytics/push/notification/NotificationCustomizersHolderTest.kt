package io.appmetrica.analytics.push.notification

import androidx.core.app.NotificationCompat
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import org.mockito.kotlin.mock

class NotificationCustomizersHolderTest {

    @Test
    fun getUserExtensions() {
        val holder = NotificationCustomizersHolder()
        val oneAgrMethod: NotificationCompat.Builder.(Int) -> NotificationCompat.Builder = mock()
        val twoAgrsMethod: NotificationCompat.Builder.(Int, Int) -> NotificationCompat.Builder = mock()
        val threeAgrsMethod: NotificationCompat.Builder.(Int, Int, Int) -> NotificationCompat.Builder = mock()
        val listMethod: NotificationCompat.Builder.(Int) -> NotificationCompat.Builder = mock()

        SoftAssertions().apply {
            assertThat(holder.customizers).isEmpty()

            holder.useProviderFor(oneAgrMethod, mock())
            assertThat(holder.customizers).hasSize(1)
            assertThat(holder.customizers[oneAgrMethod]).isNotNull

            holder.useProviderFor(twoAgrsMethod, mock())
            assertThat(holder.customizers).hasSize(2)
            assertThat(holder.customizers[twoAgrsMethod]).isNotNull

            holder.useProviderFor(threeAgrsMethod, mock())
            assertThat(holder.customizers).hasSize(3)
            assertThat(holder.customizers[threeAgrsMethod]).isNotNull

            holder.useListProviderFor(listMethod, mock())
            assertThat(holder.customizers).hasSize(4)
            assertThat(holder.customizers[listMethod]).isNotNull

            assertAll()
        }
    }

    @Test
    fun getExtraBundleProvider() {
        val holder = NotificationCustomizersHolder()
        val extraBundleProvider: ExtraBundleProvider = mock()

        holder.useExtraBundleProvider(extraBundleProvider)
        assertThat(holder.extraBundleProvider).isSameAs(extraBundleProvider)
    }
}
