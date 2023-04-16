package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.model.Flat;
import se.ifmo.lab06.server.util.IOProvider;

public class ShowCommand extends Command {
    public ShowCommand(IOProvider provider, CollectionManager collection) {
        super("show", "вывести все элементы коллекции в строковом представлении", provider, collection);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);
        String line = "-".repeat(60);
        provider.getPrinter().print(line);
        for (Flat flat : collection.getCollection()) {
            provider.getPrinter().print(flat.toString());
            provider.getPrinter().print(line);
        }
    }
}
