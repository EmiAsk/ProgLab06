package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.model.Flat;
import se.ifmo.lab06.server.util.IOProvider;

public class UpdateCommand extends Command {
    public UpdateCommand(IOProvider provider, CollectionManager collection) {
        super("update {id} {element}", "обновить значение элемента коллекции, id которого равен заданному",
                provider, collection);
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
        Flat flat = collection.get(flatId);
        if (flat == null) {
            provider.getPrinter().print("Flat with specified ID doesn't exist.");
            return;
        }
        String line = "-".repeat(60);
        provider.getPrinter().print("Chosen Flat:");
        provider.getPrinter().printf("%s\n%s\n%s\n", line, flat, line);
        FlatParser argParser = new FlatParser(provider.getScanner(), provider.getPrinter());
        Flat newFlat = argParser.parseFlat();
        collection.update(flatId, newFlat);
        provider.getPrinter().printf("Flat (ID %s) updated successfully.\n", flatId);
    }
}
