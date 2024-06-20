package io.appmetrica.analytics.push.impl.command;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import java.util.HashMap;
import java.util.Map;

public class CommandHolder {

    @NonNull
    private final Map<String, Command> commands = new HashMap<String, Command>();

    public CommandHolder() {
        commands.put(PushServiceFacade.COMMAND_INIT_PUSH_SERVICE, new InitPushServiceCommand());
        commands.put(PushServiceFacade.COMMAND_INIT_PUSH_TOKEN, new InitPushTokenCommand());
        commands.put(
            PushServiceFacade.COMMAND_UPDATE_TOKEN,
            new CommandWithProcessingMinTime(new UpdatePushTokenCommand(), new SendTokenProcessingMinTimeProvider())
        );
        commands.put(
            PushServiceFacade.COMMAND_PROCESS_PUSH,
            new CommandWithProcessingMinTime(new ProcessPushCommand(), new ProcessPushCommandMinTimeProvider())
        );
        commands.put(
            PushServiceFacade.COMMAND_SEND_PUSH_TOKEN_ON_REFRESH,
            new CommandWithProcessingMinTime(new SendTokenCommand(), new SendTokenProcessingMinTimeProvider())
        );
        commands.put(PushServiceFacade.COMMAND_SEND_PUSH_TOKEN_MANUALLY, new SendTokenCommand());
    }

    @Nullable
    public Command get(@Nullable final String action) {
        return commands.get(action);
    }
}
