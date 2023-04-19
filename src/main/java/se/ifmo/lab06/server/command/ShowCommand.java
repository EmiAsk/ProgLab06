package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;
import se.ifmo.lab06.shared.model.Flat;
import se.ifmo.lab06.server.util.IOProvider;

public class ShowCommand extends Command {
    public ShowCommand(IOProvider provider, CollectionManager collection) {
        super("show", "вывести все элементы коллекции в строковом представлении", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());
        var builder = new StringBuilder();
        String line = "-".repeat(60);
        builder.append(line).append("\n");
        for (Flat flat : collection.getCollection()) {
            builder.append(flat.toString()).append("\n");
            builder.append(line).append("\n");
        }
        return new CommandResponse(builder.toString());
    }
}
