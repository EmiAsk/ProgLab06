package se.ifmo.lab06.command;

import se.ifmo.lab06.manager.CollectionManager;
import se.ifmo.lab06.util.IOProvider;
import se.ifmo.lab06.exception.InvalidArgsException;
import se.ifmo.lab06.util.ArgumentValidator;
import se.ifmo.lab06.dto.request.CommandRequest;
import se.ifmo.lab06.dto.response.Response;

public abstract class Command {

    private static final Class<?>[] ARGS = new Class<?>[]{};

    private final String name;
    private final String description;
    IOProvider provider;
    CollectionManager collection;
    boolean requiresModel;

    public Command(String name, String description, IOProvider provider, CollectionManager collection) {
        this.name = name;
        this.description = description;
        this.provider = provider;
        this.collection = collection;
        this.requiresModel = false;
    }

    public abstract Response execute(CommandRequest args) throws InvalidArgsException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return String.format("%40s    |     %s", name, description);
    }

    public boolean isRequiresModel() {
        return requiresModel;
    }

    public void validateArgs(String[] args) throws InvalidArgsException {
        if (!ArgumentValidator.validate(getArgumentTypes(), args)) {
            throw new InvalidArgsException();
        }
    }

    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
