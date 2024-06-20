package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle

interface CommandProcessingMinTimeProvider {

    fun get(context: Context, bundle: Bundle): Long
}
