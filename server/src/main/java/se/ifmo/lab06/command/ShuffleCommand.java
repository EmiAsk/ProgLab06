package se.ifmo.lab06.command;

import se.ifmo.lab06.exception.InvalidArgsException;
import se.ifmo.lab06.manager.CollectionManager;
import se.ifmo.lab06.util.IOProvider;
import se.ifmo.lab06.dto.request.CommandRequest;
import se.ifmo.lab06.dto.response.CommandResponse;
import se.ifmo.lab06.dto.response.Response;

public class ShuffleCommand extends Command {
    public ShuffleCommand(IOProvider IOProvider, CollectionManager collection) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке", IOProvider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        collection.shuffle();
        return new CommandResponse("Collection has been shuffled.");
    }
}
