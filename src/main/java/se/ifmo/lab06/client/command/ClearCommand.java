package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

public class ClearCommand extends Command {
    public ClearCommand(IOProvider provider, Client client) {
        super("clear", "очистить коллекцию", provider, client);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);

        var request = new CommandRequest(name);
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
