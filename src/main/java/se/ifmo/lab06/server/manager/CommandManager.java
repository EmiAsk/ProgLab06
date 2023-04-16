package se.ifmo.lab06.server.manager;

import se.ifmo.lab06.server.command.*;
import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.util.IOProvider;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(CollectionManager collection, IOProvider provider, int recDepth) {
        register("info", new InfoCommand(provider, collection));
        register("show", new ShowCommand(provider, collection));
        register("add", new AddCommand(provider, collection));
        register("update", new UpdateCommand(provider, collection));
        register("remove_by_id", new RemoveByIdCommand(provider, collection));
        register("clear", new ClearCommand(provider, collection));
        register("save", new SaveCommand(provider, collection));
        register("exit", new ExitCommand(provider, collection));
        register("remove_last", new RemoveLastCommand(provider, collection));
        register("add_if_min", new AddIfMinCommand(provider, collection));
        register("shuffle", new ShuffleCommand(provider, collection));
        register("remove_all_by_furnish", new RemoveByFurnishCommand(provider, collection));
        register("filter_starts_with_name", new FilterNameCommand(provider, collection));
        register("print_unique_house", new PrintUniqueHouseCommand(provider, collection));
        register("execute_script", new ExecuteScriptCommand(provider, collection, recDepth));
        register("help", new HelpCommand(provider, collection, commands));
    }


    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public boolean execute(String commandName, String[] args) throws InvalidArgsException {
        if (commands.containsKey(commandName)) {
            commands.get(commandName).execute(args);
            return true;
        }
        return false;
    }


}
