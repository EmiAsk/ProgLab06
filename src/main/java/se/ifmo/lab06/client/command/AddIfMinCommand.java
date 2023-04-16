package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.model.Flat;
import se.ifmo.lab06.client.parser.FlatParser;
import se.ifmo.lab06.client.util.IOProvider;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(IOProvider provider, Client client) {
        super("add_if_min {element}",
                "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
                provider, client);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);

        FlatParser parser = new FlatParser(provider.getScanner(), provider.getPrinter());
        Flat flat = parser.parseFlat();

        var request = new CommandRequest(name, new Object[]{flat});
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
