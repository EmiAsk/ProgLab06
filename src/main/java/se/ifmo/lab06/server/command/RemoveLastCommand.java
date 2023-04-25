package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

public class RemoveLastCommand extends Command {
    public RemoveLastCommand(IOProvider provider, CollectionManager collection) {
        super("remove_last", "удалить последний элемент из коллекции", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        collection.pop();
        return new CommandResponse("Last collection element removed successfully.");
    }
}
