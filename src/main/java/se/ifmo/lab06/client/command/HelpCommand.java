package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

import java.util.Map;

public class HelpCommand extends Command {
    private final Map<String, Command> commands;

    public HelpCommand(IOProvider provider, Client client, Map<String, Command> commands) {
        super("help", "вывести справку по доступным командам", provider, client);
        this.commands = commands;
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);
        String line = "-".repeat(120);
        provider.getPrinter().print(line);
        for (Command command : commands.values()) {
            provider.getPrinter().print(command.getDescription());
            provider.getPrinter().print(line);
        }
    }
}
