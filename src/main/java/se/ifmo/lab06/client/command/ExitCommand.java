package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.exception.ExitException;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

public class ExitCommand extends Command {
    public ExitCommand(IOProvider provider, Client client) {
        super("exit", "завершить программу (без сохранения в файл)", provider, client);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);
        throw new ExitException();
    }
}
