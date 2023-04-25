package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

import java.util.Map;

public class HelpCommand extends Command {
    private final Map<String, Command> commands;

    public HelpCommand(IOProvider provider, CollectionManager collection, Map<String, Command> commands) {
        super("help", "вывести справку по доступным командам", provider, collection);
        this.commands = commands;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        String line = "-".repeat(120);
        var builder = new StringBuilder();
        builder.append(line).append("\n");
        for (Command command : commands.values()) {
            builder.append(command.getDescription()).append("\n");
            builder.append(line).append("\n");
        }
        return new CommandResponse(builder.toString());
    }
}
