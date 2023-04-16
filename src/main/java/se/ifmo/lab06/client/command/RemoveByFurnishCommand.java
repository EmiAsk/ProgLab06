package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.model.Furnish;
import se.ifmo.lab06.client.util.IOProvider;

public class RemoveByFurnishCommand extends Command {
    public RemoveByFurnishCommand(IOProvider provider, Client client) {
        super("remove_all_by_furnish {furnish}",
                "удалить из коллекции все элементы, значение поля furnish которого эквивалентно заданному",
                provider, client);
    }

    @Override
    public void validateArgs(String[] args, int length) throws InvalidArgsException {
        super.validateArgs(args, length);
        try {
            Furnish.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgsException();
        }
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 1);
        Furnish furnish = Furnish.valueOf(args[0]);

        var request = new CommandRequest(name, new Object[]{furnish});
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
