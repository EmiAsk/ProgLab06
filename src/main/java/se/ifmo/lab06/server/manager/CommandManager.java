package se.ifmo.lab06.server.manager;

import se.ifmo.lab06.server.command.*;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.shared.dto.CommandDTO;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.request.ValidationRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.Response;
import se.ifmo.lab06.shared.dto.response.ValidationResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(CollectionManager collection, IOProvider provider, int recDepth) {
        register("info", new InfoCommand(provider, collection));
        register("show", new ShowCommand(provider, collection));
        register("add", new AddCommand(provider, collection));
        register("update", new UpdateCommand(provider, collection));
        register("remove_by_id", new RemoveByIdCommand(provider, collection));
        register("clear", new ClearCommand(provider, collection));
//        register("save", new SaveCommand(provider, collection));
//        register("exit", new ExitCommand(provider, collection));
        register("remove_last", new RemoveLastCommand(provider, collection));
        register("add_if_min", new AddIfMinCommand(provider, collection));
        register("shuffle", new ShuffleCommand(provider, collection));
        register("remove_all_by_furnish", new RemoveByFurnishCommand(provider, collection));
        register("filter_starts_with_name", new FilterNameCommand(provider, collection));
        register("print_unique_house", new PrintUniqueHouseCommand(provider, collection));
//        register("execute_script", new ExecuteScriptCommand(provider, collection, recDepth));
        register("help", new HelpCommand(provider, collection, commands));
    }

    public List<CommandDTO> getCommands() {
        return commands.entrySet()
                .stream()
                .map(c -> new CommandDTO(c.getKey(), c.getValue().getDescription(), c.getValue().isRequiresModel()))
                .toList();
    }


    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Response execute(CommandRequest request) {
        try {
            if (commands.containsKey(request.name())) {
                return commands.get(request.name()).execute(request);
            }
            return new CommandResponse("Invalid command", StatusCode.ERROR);
        } catch (InvalidArgsException e) {
            return new CommandResponse(e.getMessage(), StatusCode.ERROR);
        }
    }

    public Response validate(ValidationRequest request) {
        try {
            if (commands.containsKey(request.commandName())) {
                var command = commands.get(request.commandName());
                System.out.println(Arrays.toString(request.args()));
                System.out.println(command.getArgNumber());
                command.validateArgs(request.args(), command.getArgNumber());
            }
            return new ValidationResponse("OK", StatusCode.OK);
        } catch (InvalidArgsException e) {
            return new ValidationResponse(e.getMessage(), StatusCode.ERROR);
        }
    }


}
