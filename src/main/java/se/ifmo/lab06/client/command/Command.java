package se.ifmo.lab06.client.command;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

public abstract class Command {
    String name;
    String description;
    IOProvider provider;
    Client client;

    public Command(String name, String description, IOProvider provider, Client client) {
        this.name = name;
        this.description = description;
        this.provider = provider;
        this.client = client;
    }

    public abstract void execute(String[] args) throws InvalidArgsException;

    public String getDescription() {
        return String.format("%s    |     %s", name, description);
    }

    public String getName() {
        return name;
    }

    public void validateArgs(String[] args, int length) throws InvalidArgsException {
        if (args.length != length) {
            throw new InvalidArgsException();
        }
    }
}
