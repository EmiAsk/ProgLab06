package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

public class ClearCommand extends Command {
    public ClearCommand(IOProvider provider, CollectionManager collection) {
        super("clear", "очистить коллекцию", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        collection.clear();
        return new CommandResponse("Collection cleared successfully.\n");
    }
}
