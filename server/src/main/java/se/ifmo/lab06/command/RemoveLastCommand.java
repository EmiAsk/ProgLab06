package se.ifmo.lab06.command;

import se.ifmo.lab06.manager.CollectionManager;
import se.ifmo.lab06.util.IOProvider;
import se.ifmo.lab06.exception.InvalidArgsException;
import se.ifmo.lab06.dto.request.CommandRequest;
import se.ifmo.lab06.dto.response.CommandResponse;
import se.ifmo.lab06.dto.response.Response;

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
