package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.exception.InvalidArgsException;

public class RemoveByIdCommand extends Command {
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
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 1);

        long flatId = Long.parseLong(args[0]);
        if (collection.get(flatId) == null) {
            provider.getPrinter().print("Flat with specified ID doesn't exist.");
            return;
        }
        collection.removeById(flatId);
        provider.getPrinter().printf("Flat (ID %s) removed successfully.\n", flatId);
    }
}
