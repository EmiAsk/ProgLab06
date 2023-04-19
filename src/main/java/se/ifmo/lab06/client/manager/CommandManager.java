package se.ifmo.lab06.client.manager;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;
import se.ifmo.lab06.shared.dto.CommandDTO;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.request.Request;
import se.ifmo.lab06.shared.dto.request.ValidationRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandManager {
    private static final Map<String, CommandDTO> commands = new HashMap<>();

    public CommandManager(Client client, IOProvider provider, int recDepth) {
//        register("info", new InfoCommand(provider, client));
//        register("show", new ShowCommand(provider, client));
//        register("add", new AddCommand(client, provider));
//        register("update", new UpdateCommand(provider, client));
//        register("remove_by_id", new RemoveByIdCommand(provider, client));
//        register("clear", new ClearCommand(provider, client));
//        register("exit", new ExitCommand(provider, client));
//        register("remove_last", new RemoveLastCommand(provider, client));
//        register("add_if_min", new AddIfMinCommand(provider, client));
//        register("shuffle", new ShuffleCommand(provider, client));
//        register("remove_all_by_furnish", new RemoveByFurnishCommand(provider, client));
//        register("filter_starts_with_name", new FilterNameCommand(provider, client));
//        register("print_unique_house", new PrintUniqueHouseCommand(provider, client));
//        register("execute_script", new ExecuteScriptCommand(provider, client, recDepth));
//        register("help", new HelpCommand(provider, client, commands));
    }


    public static void register(List<CommandDTO> commandsToAdd) {
        commands.putAll(commandsToAdd.stream().collect(Collectors.toMap(CommandDTO::name, c -> c)));
    }

    public Optional<CommandDTO> get(String commandName) {
        return Optional.ofNullable(commands.get(commandName));
    }
}
