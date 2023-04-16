package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.ExitException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.exception.InvalidArgsException;

public class ExitCommand extends Command {
    public ExitCommand(IOProvider provider, CollectionManager collection) {
        super("exit", "завершить программу (без сохранения в файл)", provider, collection);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);
        throw new ExitException();
    }
}
