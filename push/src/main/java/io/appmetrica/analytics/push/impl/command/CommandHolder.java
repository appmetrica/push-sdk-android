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
            new CommandWithProcessingMinTime(new UpdatePushTokenCommand())
        );
        commands.put(
            PushServiceFacade.COMMAND_PROCESS_PUSH,
            new CommandWithProcessingMinTime(new ProcessPushCommand())
        );
    }

    @Nullable
    public Command get(@Nullable final String action) {
        return commands.get(action);
    }
}
