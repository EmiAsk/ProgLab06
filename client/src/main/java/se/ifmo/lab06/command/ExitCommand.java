package se.ifmo.lab06.command;

import se.ifmo.lab06.Client;
import se.ifmo.lab06.exception.ExitException;
import se.ifmo.lab06.exception.InvalidArgsException;
import se.ifmo.lab06.util.IOProvider;

public class ExitCommand extends Command {
    public ExitCommand(IOProvider provider, Client client) {
        super("exit", "завершить программу (без сохранения в файл)", provider, client);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args);
        throw new ExitException();
    }
}
