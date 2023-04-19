package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;
import se.ifmo.lab06.shared.model.Flat;

public class UpdateCommand extends Command {
    
    private static final int ARGS = 1;
    public UpdateCommand(IOProvider provider, CollectionManager collection) {
        super("update {id} {element}", "обновить значение элемента коллекции, id которого равен заданному",
                provider, collection);
        this.requiresModel = true;
    }

    @Override
    public void validateArgs(String[] args, int length) throws InvalidArgsException {
        try {
            super.validateArgs(args, length);
            long id = Long.parseLong(args[0]);
            System.out.println(collection.get(id));
            if (collection.get(id) == null) {
                throw new InvalidArgsException("Flat with specified ID doesn't exist");
            }

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidArgsException();
        }
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());

        long flatId = Long.parseLong(request.args()[0]);
        Flat flat = collection.get(flatId);
        if (flat == null) {
            return new CommandResponse("Flat with specified ID doesn't exist.", StatusCode.ERROR);
        }

        var builder = new StringBuilder();
        String line = "-".repeat(60);
//        builder.append("Chosen Flat:");
//        builder.append("%s\n%s\n%s\n".formatted(line, flat, line));
        Flat newFlat = request.model();
        collection.update(flatId, newFlat);
        builder.append("Flat (ID %s) updated successfully.".formatted(flatId));
        return new CommandResponse(builder.toString());
    }

    @Override
    public int getArgNumber() {
        return ARGS;
    }
}
