package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

public class ShuffleCommand extends Command {
    public ShuffleCommand(IOProvider IOProvider, CollectionManager collection) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке", IOProvider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());
        collection.shuffle();
        return new CommandResponse("Collection has been shuffled.");
    }
}
