package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;
import se.ifmo.lab06.shared.model.Flat;
import se.ifmo.lab06.server.util.IOProvider;

import java.util.List;

public class FilterNameCommand extends Command {

    private static final int ARGS = 1;

    public FilterNameCommand(IOProvider provider, CollectionManager collection) {
        super("filter_starts_with_name {name}",
                "вывести элементы, значение поля name которых начинается с заданной подстроки",
                provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());
        String name = request.args()[0];
        List<Flat> flats = collection.getCollection()
                .stream()
                .filter(flat -> flat.getName().toLowerCase().startsWith(name.toLowerCase()))
                .toList();

        var builder = new StringBuilder();
        builder.append("Collection filtered by name:\n");
        var line = "-".repeat(60) + "\n";
        builder.append(line);
        for (Flat flat : flats) {
            builder.append(flat.toString()).append("\n");
            builder.append(line);
        }
        return new CommandResponse(builder.toString());
    }

    @Override
    public int getArgNumber() {
        return ARGS;
    }
}
