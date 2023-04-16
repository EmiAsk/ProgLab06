package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

public class RemoveLastCommand extends Command {
    public RemoveLastCommand(IOProvider provider, Client client) {
        super("remove_last", "удалить последний элемент из коллекции", provider, client);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);

        var request = new CommandRequest(name);
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
