package io.appmetrica.analytics.push.internal.service

import android.content.Context
import android.content.Intent
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.command.CommandHolder
import io.appmetrica.analytics.push.impl.utils.CommandReporter
import io.appmetrica.analytics.push.impl.utils.Utils
import io.appmetrica.analytics.push.logger.internal.DebugLogger
import io.appmetrica.analytics.push.logger.internal.PublicLogger
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object FakeService {

    private val tag = "[FakeService]"
    private val commandHolder = CommandHolder()
    private val executor: Executor = Executors.newSingleThreadExecutor()

    @JvmStatic
    fun onStartCommand(context: Context, intent: Intent) {
        try {
            val action = intent.getStringExtra(PushServiceFacade.EXTRA_COMMAND)
            DebugLogger.info(tag, "Handle command: %s", action)
            CommandReporter.reportCommandTimeDifference(
                action,
                intent.getLongExtra(
                    PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME,
                    CommandReporter.EXTRA_COMMAND_RECEIVED_TIME_DEFAULT_VALUE
                ),
                Utils.extractPushId(intent.extras!!),
                "FakeService"
            )
            val command = commandHolder[action]
            if (command != null) {
                executor.execute { command.execute(context, intent.extras!!) }
            }
        } catch (e: Throwable) {
            TrackersHub.getInstance().reportError("Failed to handle command ", e)
            PublicLogger.error(
                e,
                "An unexpected error occurred while running the AppMetrica Push SDK. " +
                    "You can report it via https://appmetrica.io/docs/troubleshooting/other.html"
            )
        }
    }
}
