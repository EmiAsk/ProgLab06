package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;
import se.ifmo.lab06.shared.model.Flat;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(IOProvider provider, CollectionManager collection) {
        super("add_if_min {element}",
                "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
                provider, collection);
        this.requiresModel = true;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args(), getArgNumber());

//        Flat flat = n;
//        // TODO: обработка конца файла или ввода NoSuchElementException3
        var flat = request.model();
        var minFlat = collection.min();
        if (minFlat.getArea() <= flat.getArea()) {
            var message = "Flat (value: %s) not added because there is flat with less value (%s).\n".formatted(
                    flat.getArea(), minFlat.getArea()
            );
            return new CommandResponse(message);
        }
        collection.push(flat);
        var message = "Flat (ID %s) added successfully.\n".formatted(flat.getId());
        return new CommandResponse(message);
    }
}
