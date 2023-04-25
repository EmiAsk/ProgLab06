package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

public class RemoveByIdCommand extends Command {
    
    private static final Class<?>[] ARGS = new Class<?>[]{Long.class};
    
    public RemoveByIdCommand(IOProvider provider, CollectionManager collection) {
        super("remove_by_id {id}", "удалить элемент из коллекции по его id", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        long flatId = Long.parseLong(request.args()[0]);
        if (collection.get(flatId) == null) {
            return new CommandResponse("Flat with specified ID doesn't exist.", StatusCode.ERROR);
        }
        collection.removeById(flatId);
        return new CommandResponse("Flat (ID %s) removed successfully.\n".formatted(flatId));
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
