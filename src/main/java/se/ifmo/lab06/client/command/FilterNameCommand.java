package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.dto.CommandRequest;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

public class FilterNameCommand extends Command {
    public FilterNameCommand(IOProvider provider, Client client) {
        super("filter_starts_with_name {name}",
                "вывести элементы, значение поля name которых начинается с заданной подстроки",
                provider, client);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 1);

        var request = new CommandRequest(name, new Object[]{args[0]});
        var response = client.sendAndReceive(request);

        provider.getPrinter().print(response.message());
    }
}
