package io.appmetrica.analytics.push.coreutils.internal

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.commands.PushTokenCommandInfo
import io.appmetrica.analytics.push.coreutils.internal.commands.SystemInfoCommandInfo
import io.appmetrica.analytics.push.coreutils.internal.model.ServiceType
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PushServiceFacadeTest : CommonTest() {

    private val context: Context = mock()
    private val commandServiceWrapper: CommandServiceWrapper = mock()

    @Before
    fun setUp() {
        PushServiceFacade.setJobIntentServiceWrapper(commandServiceWrapper)
    }

    @Test
    fun initPushService() {
        PushServiceFacade.initPushService(context)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(true))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.InitPushService.COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()
    }

    @Test
    fun initToken() {
        val provider = "some provider"
        PushServiceFacade.initToken(context, provider)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(true))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.INIT_PUSH_TOKEN_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isNull()
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun sendToken() {
        val token = "Some token"
        val provider = "some transport"
        PushServiceFacade.sendToken(context, provider, token)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.UPDATE_PUSH_TOKEN_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isEqualTo(token)
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun sendTokenIfTokenIsNull() {
        val provider = "some transport"
        PushServiceFacade.sendToken(context, provider, null)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.UPDATE_PUSH_TOKEN_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isNull()
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun sendTokenOnRefresh() {
        val token = "Some token"
        val provider = "some transport"
        PushServiceFacade.sendTokenOnRefresh(context, provider, token)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.SEND_PUSH_TOKEN_ON_REFRESH_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isEqualTo(token)
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun sendTokenOnRefreshIfTokenIsNull() {
        val provider = "some transport"
        PushServiceFacade.sendTokenOnRefresh(context, provider, null)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.SEND_PUSH_TOKEN_ON_REFRESH_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isNull()
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun sendTokenManually() {
        val token = "Some token"
        val provider = "some transport"
        PushServiceFacade.sendTokenManually(context, provider, token)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.SEND_PUSH_TOKEN_MANUALLY_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isEqualTo(token)
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun sendTokenManuallyIfTokenIsNull() {
        val provider = "some transport"
        PushServiceFacade.sendTokenManually(context, provider, null)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.SendPushToken.SEND_PUSH_TOKEN_MANUALLY_COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val pushTokenCommandInfoBundle = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)!!
        val pushTokenCommandInfo = PushTokenCommandInfo.fromBundle(pushTokenCommandInfoBundle)

        assertThat(pushTokenCommandInfo.provider).isEqualTo(provider)
        assertThat(pushTokenCommandInfo.token).isNull()
        assertThat(pushTokenCommandInfo.force).isFalse()
    }

    @Test
    fun processPushIfNeedService() {
        val transport = "my_transport"
        val processingMinTime = 13L
        val pushMessageBundle = Bundle().apply {
            putString(
                CoreConstants.PushMessage.ROOT_ELEMENT,
                JSONObject()
                    .put(CoreConstants.PushMessage.PROCESSING_MIN_TIME, processingMinTime)
                    .toString()
            )
        }

        PushServiceFacade.processPush(context, pushMessageBundle, transport)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(true))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.ProcessPush.COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()
        assertThat(bundle.getString(CoreConstants.EXTRA_TRANSPORT))
            .isEqualTo(transport)
        assertThat(bundle.getLong(CoreConstants.MIN_PROCESSING_DELAY))
            .isEqualTo(processingMinTime)
        assertThat(bundle.keySet()).containsAnyElementsOf(pushMessageBundle.keySet())
    }

    @Test
    fun processPushIfNoNeedService() {
        val transport = "my_transport"
        val processingMinTime = 13L
        val pushMessageBundle = Bundle().apply {
            putString(
                CoreConstants.PushMessage.ROOT_ELEMENT,
                JSONObject()
                    .put(CoreConstants.PushMessage.PROCESSING_MIN_TIME, processingMinTime)
                    .put(CoreConstants.PushMessage.SERVICE_TYPE, ServiceType.PROVIDER_SERVICE.value)
                    .toString()
            )
        }

        PushServiceFacade.processPush(context, pushMessageBundle, transport)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(false))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.ProcessPush.COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()
        assertThat(bundle.getString(CoreConstants.EXTRA_TRANSPORT))
            .isEqualTo(transport)
        assertThat(bundle.getLong(CoreConstants.MIN_PROCESSING_DELAY))
            .isEqualTo(processingMinTime)
        assertThat(bundle.keySet()).containsAnyElementsOf(pushMessageBundle.keySet())
    }

    @Test
    fun processPushIfNoProcessingMinTime() {
        val transport = "my_transport"
        val pushMessageBundle = Bundle().apply {
            putString(
                CoreConstants.PushMessage.ROOT_ELEMENT,
                JSONObject().toString()
            )
        }

        PushServiceFacade.processPush(context, pushMessageBundle, transport)
        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(true))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.ProcessPush.COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()
        assertThat(bundle.getString(CoreConstants.EXTRA_TRANSPORT))
            .isEqualTo(transport)
        assertThat(bundle.containsKey(CoreConstants.MIN_PROCESSING_DELAY)).isFalse()
        assertThat(bundle.keySet()).containsAnyElementsOf(pushMessageBundle.keySet())
    }

    @Test
    fun processPushIfNotOwnPush() {
        PushServiceFacade.processPush(context, Bundle(), "transport")

        verifyNoInteractions(commandServiceWrapper)
    }

    @Test
    fun sendSystemInfo() {
        val statusChangeTime = 24523L
        PushServiceFacade.sendSystemInfo(context, statusChangeTime)

        val arg = argumentCaptor<Bundle>()

        verify(commandServiceWrapper).startCommand(eq(context), arg.capture(), eq(true))

        val bundle = arg.firstValue

        assertThat(bundle.getString(Commands.EXTRA_COMMAND))
            .isEqualTo(Commands.UpdateSystemInfo.COMMAND_ACTION)
        assertThat(bundle.containsKey(Commands.EXTRA_COMMAND_RECEIVED_TIME)).isTrue()

        val systemInfoCommandInfoBundle = bundle.getBundle(Commands.UpdateSystemInfo.EXTRA_INFO)!!
        val systemInfoCommandInfo = SystemInfoCommandInfo.fromBundle(systemInfoCommandInfoBundle)

        assertThat(systemInfoCommandInfo.statusChangeTime).isEqualTo(statusChangeTime)
    }
}
