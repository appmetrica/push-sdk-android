package io.appmetrica.analytics.push.coreutils.internal

import android.content.Context
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.commands.PushTokenCommandInfo
import io.appmetrica.analytics.push.coreutils.internal.commands.SystemInfoCommandInfo
import io.appmetrica.analytics.push.coreutils.internal.model.ServiceType
import io.appmetrica.analytics.push.coreutils.internal.model.TransportPushMessage
import io.appmetrica.analytics.push.logger.internal.DebugLogger

object PushServiceFacade {

    private const val TAG = "[PushServiceFacade]"

    private var commandServiceWrapper = CommandServiceWrapper()

    @JvmStatic
    fun initPushService(context: Context) {
        DebugLogger.info(TAG, "Init push service")
        val bundle = Bundle().apply {
            putCommand(Commands.InitPushService.COMMAND_ACTION)
        }
        commandServiceWrapper.startCommand(context, bundle)
    }

    @JvmStatic
    fun initToken(context: Context, provider: String) {
        DebugLogger.info(TAG, "Init token from provider: $provider")
        val bundle = Bundle().apply {
            putCommand(Commands.SendPushToken.INIT_PUSH_TOKEN_COMMAND_ACTION)
            putBundle(
                Commands.SendPushToken.EXTRA_INFO,
                PushTokenCommandInfo.Builder(provider)
                    .build()
                    .toBundle()
            )
        }
        commandServiceWrapper.startCommand(context, bundle)
    }

    @JvmStatic
    fun sendToken(context: Context, provider: String, token: String?) {
        DebugLogger.info(TAG, "Send token from provider: $provider")
        val bundle = Bundle().apply {
            putCommand(Commands.SendPushToken.UPDATE_PUSH_TOKEN_COMMAND_ACTION)
            putBundle(
                Commands.SendPushToken.EXTRA_INFO,
                PushTokenCommandInfo.Builder(provider)
                    .withToken(token)
                    .build()
                    .toBundle()
            )
        }
        commandServiceWrapper.startCommand(context, bundle, needService = false)
    }

    @JvmStatic
    fun sendTokenOnRefresh(context: Context, provider: String, token: String?) {
        DebugLogger.info(TAG, "Send token on refresh from provider: $provider")
        val bundle = Bundle().apply {
            putCommand(Commands.SendPushToken.SEND_PUSH_TOKEN_ON_REFRESH_COMMAND_ACTION)
            putBundle(
                Commands.SendPushToken.EXTRA_INFO,
                PushTokenCommandInfo.Builder(provider)
                    .withToken(token)
                    .build()
                    .toBundle()
            )
        }
        commandServiceWrapper.startCommand(context, bundle, needService = false)
    }

    @JvmStatic
    fun sendTokenManually(context: Context, provider: String, token: String?) {
        DebugLogger.info(TAG, "Send token manually from provider: $provider")
        val bundle = Bundle().apply {
            putCommand(Commands.SendPushToken.SEND_PUSH_TOKEN_MANUALLY_COMMAND_ACTION)
            putBundle(
                Commands.SendPushToken.EXTRA_INFO,
                PushTokenCommandInfo.Builder(provider)
                    .withToken(token)
                    .build()
                    .toBundle()
            )
        }
        commandServiceWrapper.startCommand(context, bundle, needService = false)
    }

    @JvmStatic
    fun processPush(
        context: Context,
        data: Bundle,
        transport: String
    ) {
        DebugLogger.info(TAG, "Process push")
        val bundle = Bundle().apply {
            putCommand(Commands.ProcessPush.COMMAND_ACTION)
            putAll(data)
            putString(CoreConstants.EXTRA_TRANSPORT, transport)
        }
        val transportPushMessage = TransportPushMessage(bundle)
        transportPushMessage.processingMinTime?.let { processingMinTime ->
            bundle.putLong(CoreConstants.MIN_PROCESSING_DELAY, processingMinTime)
        }
        if (transportPushMessage.isOwnPush) {
            commandServiceWrapper.startCommand(context, bundle, needService(transportPushMessage))
        }
    }

    @JvmStatic
    fun sendSystemInfo(
        context: Context,
        statusChangeTime: Long?
    ) {
        DebugLogger.info(TAG, "Send system info with change time: $statusChangeTime")
        val bundle = Bundle().apply {
            putCommand(Commands.UpdateSystemInfo.COMMAND_ACTION)
            putBundle(
                Commands.UpdateSystemInfo.EXTRA_INFO,
                SystemInfoCommandInfo.Builder()
                    .withStatusChangeTime(statusChangeTime)
                    .build()
                    .toBundle()
            )
        }
        commandServiceWrapper.startCommand(context, bundle)
    }

    @JvmStatic
    @VisibleForTesting
    fun setJobIntentServiceWrapper(commandServiceWrapper: CommandServiceWrapper) {
        PushServiceFacade.commandServiceWrapper = commandServiceWrapper
    }

    private fun needService(transportPushMessage: TransportPushMessage): Boolean {
        return when (transportPushMessage.serviceType) {
            ServiceType.PROVIDER_SERVICE -> false
            ServiceType.APPMETRICA_PUSH_SERVICE, ServiceType.UNKNOWN -> true
            else -> true
        }
    }

    private fun Bundle.putCommand(command: String) = apply {
        putString(Commands.EXTRA_COMMAND, command)
        putLong(Commands.EXTRA_COMMAND_RECEIVED_TIME, System.currentTimeMillis())
    }
}
