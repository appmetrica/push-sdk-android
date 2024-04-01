package io.appmetrica.analytics.push.testutils

import org.junit.rules.ExternalResource

class PrintExecutorRule : ExternalResource() {

    override fun before() {
        super.before()
        println("Execute test on Gradle Test Executor #${System.getProperty("org.gradle.test.worker")}")
    }
}
