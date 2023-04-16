package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.model.Flat;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(IOProvider provider, CollectionManager collection) {
        super("add_if_min {element}",
                "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
                provider, collection);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);

        FlatParser parser = new FlatParser(provider.getScanner(), provider.getPrinter());
        Flat flat = parser.parseFlat();
        // TODO: обработка конца файла или ввода NoSuchElementException3
        Flat minFlat = collection.min();
        if (minFlat.getArea() <= flat.getArea()) {
            provider.getPrinter().printf("Flat (value: %s) not added because there is flat with less value (%s).\n",
                    flat.getArea(), minFlat.getArea());
            return;
        }
        collection.push(flat);
        provider.getPrinter().printf("Flat (ID %s) added successfully.\n", flat.getId());
    }
}
