package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;

public class AddCommand extends Command {
    public AddCommand(IOProvider provider, CollectionManager collection) {
        super("add", "добавить новый элемент в коллекцию", provider, collection);
        this.requiresModel = true;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());
//        FlatParser parser = new FlatParser(provider.getScanner(), provider.getPrinter());

//        Flat flat = parser.parseFlat();
//         TODO: обработка конца файла или ввода NoSuchElementException
        collection.push(request.model());  // TODO: Perhaps ID should be returned from push

        var message = "Flat (ID %s) added successfully.\n".formatted(request.model().getId());
        return new CommandResponse(message);
    }
}
