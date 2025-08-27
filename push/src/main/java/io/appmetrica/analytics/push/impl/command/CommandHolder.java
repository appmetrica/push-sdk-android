package io.appmetrica.analytics.push.impl.command;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands;
import io.appmetrica.analytics.push.impl.system.event.DefaultSystemInfoEventProvider;
import io.appmetrica.analytics.push.impl.system.processor.DefaultSystemInfoEventProcessor;
import io.appmetrica.analytics.push.impl.token.event.DefaultTokenEventProvider;
import io.appmetrica.analytics.push.impl.token.filter.provider.DefaultTokenEventFilterProvider;
import io.appmetrica.analytics.push.impl.token.processor.InitTokenEventProcessor;
import io.appmetrica.analytics.push.impl.token.processor.UpdateTokenEventProcessor;
import java.util.HashMap;
import java.util.Map;

public class CommandHolder {

    @NonNull
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHolder() {
        commands.put(Commands.InitPushService.COMMAND_ACTION, new InitPushServiceCommand());
        commands.put(
            Commands.SendPushToken.INIT_PUSH_TOKEN_COMMAND_ACTION,
            new SendPushTokenCommand(
                new DefaultTokenEventProvider(),
                new DefaultTokenEventFilterProvider(),
                new InitTokenEventProcessor()
            )
        );
        commands.put(
            Commands.SendPushToken.UPDATE_PUSH_TOKEN_COMMAND_ACTION,
            new CommandWithProcessingMinTime(
                new SendPushTokenCommand(
                    new DefaultTokenEventProvider(),
                    new DefaultTokenEventFilterProvider(),
                    new UpdateTokenEventProcessor()
                ),
                new SendTokenProcessingMinTimeProvider()
            )
        );
        commands.put(
            Commands.ProcessPush.COMMAND_ACTION,
            new CommandWithProcessingMinTime(
                new ProcessPushCommand(),
                new ProcessPushCommandMinTimeProvider()
            )
        );
        commands.put(
            Commands.SendPushToken.SEND_PUSH_TOKEN_ON_REFRESH_COMMAND_ACTION,
            new CommandWithProcessingMinTime(
                new SendPushTokenCommand(
                    new DefaultTokenEventProvider(),
                    new DefaultTokenEventFilterProvider(),
                    new UpdateTokenEventProcessor()
                ),
                new SendTokenProcessingMinTimeProvider()
            )
        );
        commands.put(
            Commands.SendPushToken.SEND_PUSH_TOKEN_MANUALLY_COMMAND_ACTION,
            new SendPushTokenCommand(
                new DefaultTokenEventProvider(),
                new DefaultTokenEventFilterProvider(),
                new UpdateTokenEventProcessor()
            )
        );
        commands.put(
            Commands.UpdateSystemInfo.COMMAND_ACTION,
            new UpdateSystemInfoCommand(
                new DefaultSystemInfoEventProvider(),
                new DefaultSystemInfoEventProcessor()
            )
        );
    }

    @Nullable
    public Command get(@Nullable final String action) {
        return commands.get(action);
    }
}
