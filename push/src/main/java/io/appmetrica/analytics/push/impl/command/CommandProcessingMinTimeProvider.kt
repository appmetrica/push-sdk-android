package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle

internal interface CommandProcessingMinTimeProvider {

    fun get(context: Context, bundle: Bundle): Long
}
