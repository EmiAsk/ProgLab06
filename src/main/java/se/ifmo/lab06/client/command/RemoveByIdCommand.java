package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand(IOProvider provider, Client client) {
        super("remove_by_id {id}", "удалить элемент из коллекции по его id", provider, client);
    }

    @Override
    public void validateArgs(String[] args, int length) throws InvalidArgsException {
        try {
            super.validateArgs(args, length);
            Long.parseLong(args[0]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidArgsException();
        }
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 1);

        var id = Long.parseLong(args[0]);

        var request = new CommandRequest(name, new Object[]{id});
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
