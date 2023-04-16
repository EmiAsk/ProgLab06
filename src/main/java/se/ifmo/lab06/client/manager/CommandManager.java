package se.ifmo.lab06.client.manager;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.command.*;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.util.IOProvider;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(Client client, IOProvider provider, int recDepth) {
        register("info", new InfoCommand(provider, client));
        register("show", new ShowCommand(provider, client));
        register("add", new AddCommand(client, provider));
        register("update", new UpdateCommand(provider, client));
        register("remove_by_id", new RemoveByIdCommand(provider, client));
        register("clear", new ClearCommand(provider, client));
        register("exit", new ExitCommand(provider, client));
        register("remove_last", new RemoveLastCommand(provider, client));
        register("add_if_min", new AddIfMinCommand(provider, client));
        register("shuffle", new ShuffleCommand(provider, client));
        register("remove_all_by_furnish", new RemoveByFurnishCommand(provider, client));
        register("filter_starts_with_name", new FilterNameCommand(provider, client));
        register("print_unique_house", new PrintUniqueHouseCommand(provider, client));
        register("execute_script", new ExecuteScriptCommand(provider, client, recDepth));
        register("help", new HelpCommand(provider, client, commands));
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
