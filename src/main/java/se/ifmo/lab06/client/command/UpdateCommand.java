package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.dto.Response;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.model.Flat;
import se.ifmo.lab06.client.parser.FlatParser;
import se.ifmo.lab06.client.util.IOProvider;

public class UpdateCommand extends Command {
    public UpdateCommand(IOProvider provider, Client client) {
        super("update {id} {element}", "обновить значение элемента коллекции, id которого равен заданному",
                provider, client);
    }

    @Override
    public void validateArgs(String[] args, int length) throws InvalidArgsException {
        try {
            super.validateArgs(args, length);
            long id = Long.parseLong(args[0]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidArgsException();
        }
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 1);
        long flatId = Long.parseLong(args[0]);

        var request = new CommandRequest(name, new Object[]{flatId});
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
        if (response.status() == Response.Status.ERROR) {
            return;
        }


        FlatParser argParser = new FlatParser(provider.getScanner(), provider.getPrinter());
        Flat flat = argParser.parseFlat();

        request = new CommandRequest(name, new Object[]{flat});
        response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
