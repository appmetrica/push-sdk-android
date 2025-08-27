package io.appmetrica.analytics.push.coreutils.internal.commands

object Commands {

    const val EXTRA_COMMAND = "io.appmetrica.analytics.push.extra.COMMAND"
    const val EXTRA_COMMAND_RECEIVED_TIME = "io.appmetrica.analytics.push.extra.EXTRA_COMMAND_RECEIVED_TIME"

    object InitPushService {
        const val COMMAND_ACTION = "io.appmetrica.analytics.push.command.INIT_PUSH_SERVICE"
    }

    object SendPushToken {
        const val INIT_PUSH_TOKEN_COMMAND_ACTION =
            "io.appmetrica.analytics.push.command.INIT_PUSH_TOKEN"
        const val UPDATE_PUSH_TOKEN_COMMAND_ACTION =
            "io.appmetrica.analytics.push.command.UPDATE_PUSH_TOKEN"
        const val SEND_PUSH_TOKEN_ON_REFRESH_COMMAND_ACTION =
            "io.appmetrica.analytics.push.command.SEND_PUSH_TOKEN_ON_REFRESH"
        const val SEND_PUSH_TOKEN_MANUALLY_COMMAND_ACTION =
            "io.appmetrica.analytics.push.command.SEND_PUSH_TOKEN_MANUALLY"

        const val EXTRA_INFO = "io.appmetrica.analytics.push.extra.EXTRA_INFO"
    }

    object ProcessPush {
        const val COMMAND_ACTION = "io.appmetrica.analytics.push.command.PROCESS_PUSH"
    }

    object UpdateSystemInfo {
        const val COMMAND_ACTION = "io.appmetrica.analytics.push.command.UPDATE_SYSTEM_INFO"

        const val EXTRA_INFO = "io.appmetrica.analytics.push.extra.EXTRA_INFO"
    }
}
