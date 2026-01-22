package io.appmetrica.analytics.push.impl.notification

import androidx.core.app.NotificationCompat
import io.appmetrica.analytics.push.model.PushMessage
import io.appmetrica.analytics.push.notification.NotificationValueProvider
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.Rand.randomInt
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

internal class NotificationBuilderMethodInvokerTest : CommonTest() {

    private val builder: NotificationCompat.Builder = mock()
    private val pushMessage: PushMessage = mock()

    @Test
    fun invokeWithOneT() {
        val method: NotificationCompat.Builder.(Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<Int> = mock()

        val value = randomInt()

        whenever(provider.get(pushMessage)).thenReturn(value)

        NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage)
        verify(method).invoke(builder, value)
    }

    @Test
    fun invokeWithOneTIfValueIsNull() {
        val method: NotificationCompat.Builder.(Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<Int> = mock()

        whenever(provider.get(pushMessage)).thenReturn(null)

        NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage)
        verifyNoInteractions(method)
    }

    @Test
    fun invokeWithTwoT() {
        val method: NotificationCompat.Builder.(Int, Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<List<Int>> = mock()

        val value1 = randomInt()
        val value2 = randomInt()

        whenever(provider.get(pushMessage)).thenReturn(listOf(value1, value2))

        NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage)
        verify(method).invoke(builder, value1, value2)
    }

    @Test
    fun invokeWithTwoTIfValueIsNull() {
        val method: NotificationCompat.Builder.(Int, Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<List<Int>> = mock()

        whenever(provider.get(pushMessage)).thenReturn(null)

        NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage)
        verifyNoInteractions(method)
    }

    @Test
    fun invokeWithThreeT() {
        val method: NotificationCompat.Builder.(Int, Int, Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<List<Int>> = mock()

        val value1 = randomInt()
        val value2 = randomInt()
        val value3 = randomInt()

        whenever(provider.get(pushMessage)).thenReturn(listOf(value1, value2, value3))

        NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage)
        verify(method).invoke(builder, value1, value2, value3)
    }

    @Test
    fun invokeWithThreeTIfValueIsNull() {
        val method: NotificationCompat.Builder.(Int, Int, Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<List<Int>> = mock()

        whenever(provider.get(pushMessage)).thenReturn(null)

        NotificationBuilderMethodInvoker.invoke(method, provider, builder, pushMessage)
        verifyNoInteractions(method)
    }

    @Test
    fun invokeWithList() {
        val method: NotificationCompat.Builder.(Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<List<Int>> = mock()

        val value1 = randomInt()
        val value2 = randomInt()
        val value3 = randomInt()

        whenever(provider.get(pushMessage)).thenReturn(listOf(value1, value2, value3))

        NotificationBuilderMethodInvoker.invokeWithList(method, provider, builder, pushMessage)
        verify(method).invoke(builder, value1)
        verify(method).invoke(builder, value2)
        verify(method).invoke(builder, value3)
    }

    @Test
    fun invokeWithListIfValueIsNull() {
        val method: NotificationCompat.Builder.(Int) -> NotificationCompat.Builder = mock()
        val provider: NotificationValueProvider<List<Int>> = mock()

        whenever(provider.get(pushMessage)).thenReturn(null)

        NotificationBuilderMethodInvoker.invokeWithList(method, provider, builder, pushMessage)
        verifyNoInteractions(method)
    }
}
