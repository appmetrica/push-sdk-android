package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CommandWithProcessingMinTime(
    val command: Command
) : Command {

    private val countDownLatch = CountDownLatch(1)
    private val handler = Handler(Looper.getMainLooper())
    private val processingMinTimeNormalizer = ProcessingMinTimeNormalizer()

    override fun execute(context: Context, bundle: Bundle) {
        val minProcessingDelaySeconds = processingMinTimeNormalizer.normalize(
            context,
            bundle.getLong(CoreConstants.MIN_PROCESSING_DELAY, -1),
            bundle.getString(CoreConstants.EXTRA_TRANSPORT, CoreConstants.Transport.UNKNOWN)
        )
        PLog.i("Execute command $command with $minProcessingDelaySeconds seconds min processing time")

        handler.postDelayed({ countDownLatch.countDown() }, TimeUnit.SECONDS.toMillis(minProcessingDelaySeconds))

        command.execute(context, bundle)

        try {
            PLog.i("Await $command count down: ${System.currentTimeMillis()}")
            countDownLatch.await(minProcessingDelaySeconds, TimeUnit.SECONDS)
        } catch (e: Throwable) {
            PLog.e(e, e.message)
        }
        PLog.i("Command $command with delay $minProcessingDelaySeconds finished at ${System.currentTimeMillis()}")
    }
}
