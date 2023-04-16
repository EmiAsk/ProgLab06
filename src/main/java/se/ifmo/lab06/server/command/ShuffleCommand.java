package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;

public class ShuffleCommand extends Command {
    public ShuffleCommand(IOProvider IOProvider, CollectionManager collection) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке", IOProvider, collection);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);
        collection.shuffle();
        provider.getPrinter().print("Collection has been shuffled.");
    }
}
