package io.appmetrica.analytics.push.testutils

import org.junit.rules.ExternalResource
import org.robolectric.shadows.ShadowLog
import java.io.PrintStream

class LogRule : ExternalResource() {

    private var logsStream: PrintStream? = null

    override fun before() {
        logsStream = ShadowLog.stream
        ShadowLog.stream = System.out
    }

    override fun after() {
        ShadowLog.stream = logsStream
    }
}

