package io.appmetrica.analytics.push.impl.command

import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class CommandHolderTest : CommonTest() {

    private val commandHolder = CommandHolder()

    @Test
    fun `get for InitPushService`() {
        val command = commandHolder[Commands.InitPushService.COMMAND_ACTION]
        assertThat(command).isInstanceOf(InitPushServiceCommand::class.java)
    }

    @Test
    fun `get for InitPushToken`() {
        val command = commandHolder[Commands.SendPushToken.INIT_PUSH_TOKEN_COMMAND_ACTION]
        assertThat(command).isInstanceOf(SendPushTokenCommand::class.java)
        assertThat(command).isNotInstanceOf(CommandWithProcessingMinTime::class.java)
    }

    @Test
    fun `get for UpdatePushToken`() {
        val command = commandHolder[Commands.SendPushToken.UPDATE_PUSH_TOKEN_COMMAND_ACTION]
        assertThat(command).isInstanceOf(CommandWithProcessingMinTime::class.java)
        assertThat((command as CommandWithProcessingMinTime).command).isInstanceOf(SendPushTokenCommand::class.java)
    }

    @Test
    fun `get for ProcessPush`() {
        val command = commandHolder[Commands.ProcessPush.COMMAND_ACTION]
        assertThat(command).isInstanceOf(CommandWithProcessingMinTime::class.java)
        assertThat((command as CommandWithProcessingMinTime).command).isInstanceOf(ProcessPushCommand::class.java)
    }

    @Test
    fun `get for SendPushTokenOnRefresh`() {
        val command = commandHolder[Commands.SendPushToken.SEND_PUSH_TOKEN_ON_REFRESH_COMMAND_ACTION]
        assertThat(command).isInstanceOf(CommandWithProcessingMinTime::class.java)
        assertThat((command as CommandWithProcessingMinTime).command).isInstanceOf(SendPushTokenCommand::class.java)
    }

    @Test
    fun `get for SendPushTokenManually`() {
        val command = commandHolder[Commands.SendPushToken.SEND_PUSH_TOKEN_MANUALLY_COMMAND_ACTION]
        assertThat(command).isInstanceOf(SendPushTokenCommand::class.java)
        assertThat(command).isNotInstanceOf(CommandWithProcessingMinTime::class.java)
    }

    @Test
    fun `get for UpdateSystemInfo`() {
        val command = commandHolder[Commands.UpdateSystemInfo.COMMAND_ACTION]
        assertThat(command).isInstanceOf(UpdateSystemInfoCommand::class.java)
        assertThat(command).isNotInstanceOf(CommandWithProcessingMinTime::class.java)
    }

    @Test
    fun `get for unknown`() {
        assertThat(commandHolder["unknown"]).isNull()
    }
}
