package io.appmetrica.analytics.push.coreutils.internal

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceCommandLauncher
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceControllerProvider
import io.appmetrica.analytics.push.testutils.CommonTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CommandServiceWrapperTest : CommonTest() {

    private val testPackage = "com.test.package"

    private val context: Context = mock {
        on { packageName } doReturn testPackage
    }
    private val bundle = Bundle()
    private val serviceLauncher: PushServiceCommandLauncher = mock()
    private val noServiceLauncher: PushServiceCommandLauncher = mock()
    private val provider: PushServiceControllerProvider = mock {
        on { getPushServiceCommandLauncher(true) } doReturn serviceLauncher
        on { getPushServiceCommandLauncher(false) } doReturn noServiceLauncher
    }

    @Test
    fun startJobWithRightComponent() {
        val wrapper = CommandServiceWrapper()
        wrapper.setPushServiceControllerProvider(provider)
        wrapper.startCommand(context, bundle)

        verify(serviceLauncher).launchService(bundle)
    }

    @Test
    fun startJobWithRightComponentIfDoesNotNeedService() {
        val wrapper = CommandServiceWrapper()
        wrapper.setPushServiceControllerProvider(provider)
        wrapper.startCommand(context, bundle, false)

        verify(noServiceLauncher).launchService(bundle)
    }
}
