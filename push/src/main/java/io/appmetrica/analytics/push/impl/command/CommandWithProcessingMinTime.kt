package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import io.appmetrica.analytics.push.logger.internal.DebugLogger
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class CommandWithProcessingMinTime(
    val command: Command,
    private val commandProcessingMinTimeProvider: CommandProcessingMinTimeProvider
) : Command {

    private val tag = "[CommandWithProcessingMinTime]"

    private val countDownLatch = CountDownLatch(1)
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(context: Context, bundle: Bundle) {
        val minProcessingDelaySeconds = commandProcessingMinTimeProvider.get(context, bundle)
        DebugLogger.info(tag, "Execute command $command with $minProcessingDelaySeconds seconds min processing time")

        handler.postDelayed({ countDownLatch.countDown() }, TimeUnit.SECONDS.toMillis(minProcessingDelaySeconds))

        command.execute(context, bundle)

        try {
            DebugLogger.info(tag, "Await $command count down: ${System.currentTimeMillis()}")
            countDownLatch.await(minProcessingDelaySeconds, TimeUnit.SECONDS)
        } catch (e: Throwable) {
            DebugLogger.error(tag, e, e.message)
        }
        DebugLogger.info(
            tag,
            "Command $command with delay $minProcessingDelaySeconds finished at ${System.currentTimeMillis()}"
        )
    }
}
