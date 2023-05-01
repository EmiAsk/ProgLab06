package se.ifmo.lab06.command;

import lombok.SneakyThrows;
import se.ifmo.lab06.exception.InvalidArgsException;
import se.ifmo.lab06.manager.CollectionManager;
import se.ifmo.lab06.dto.request.CommandRequest;
import se.ifmo.lab06.dto.response.CommandResponse;
import se.ifmo.lab06.dto.response.Response;
import se.ifmo.lab06.model.Flat;
import se.ifmo.lab06.util.IOProvider;

public class ShowCommand extends Command {
    public ShowCommand(IOProvider provider, CollectionManager collection) {
        super("show", "вывести все элементы коллекции в строковом представлении", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
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
