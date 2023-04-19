package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

public class RemoveByIdCommand extends Command {
    
    private static final int ARGS = 1;
    
    public RemoveByIdCommand(IOProvider provider, CollectionManager collection) {
        super("remove_by_id {id}", "удалить элемент из коллекции по его id", provider, collection);
    }

    @Override
    public void validateArgs(String[] args, int length) throws InvalidArgsException {
        try {
            super.validateArgs(args, length);
            long id = Long.parseLong(args[0]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidArgsException();
        }
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());

        long flatId = Long.parseLong(request.args()[0]);
        if (collection.get(flatId) == null) {
            return new CommandResponse("Flat with specified ID doesn't exist.", StatusCode.ERROR);
        }
        collection.removeById(flatId);
        return new CommandResponse("Flat (ID %s) removed successfully.\n".formatted(flatId));
    }

    @Override
    public int getArgNumber() {
        return ARGS;
    }
}
