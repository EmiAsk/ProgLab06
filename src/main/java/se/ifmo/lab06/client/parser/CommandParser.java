package se.ifmo.lab06.client.parser;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.command.ExecuteScriptCommand;
import se.ifmo.lab06.client.command.ExitCommand;
import se.ifmo.lab06.client.exception.ExitException;
import se.ifmo.lab06.client.exception.InterruptCommandException;
import se.ifmo.lab06.client.exception.InvalidArgsException;
import se.ifmo.lab06.client.exception.RecursionException;
import se.ifmo.lab06.client.manager.CommandManager;
import se.ifmo.lab06.client.util.IOProvider;
import se.ifmo.lab06.client.util.Printer;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.request.ValidationRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.ValidationResponse;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandParser extends DefaultTypeParser {
    private final CommandManager commandManager;
    private final IOProvider provider;
    private final Client client;
    private final static int MAX_REC_DEPTH = 5;
    private final int recDepth;

    public CommandParser(Client client, CommandManager commandManager, IOProvider provider, int recDepth) {
        super(provider.getScanner(), provider.getPrinter());
        this.commandManager = commandManager;
        this.provider = provider;
        this.recDepth = recDepth;
        this.client = client;
    }

    public void run() {
        Scanner scanner = provider.getScanner();
        Printer printer = provider.getPrinter();

        if (recDepth > MAX_REC_DEPTH) {
            throw new RecursionException();
        }

        while (true) {
            try {
                printer.printf("Enter command:\n");
                String line = scanner.nextLine();
                String[] splitLine = line.strip().split("\s+");
                String commandName = splitLine[0].toLowerCase();
                String[] args = Arrays.copyOfRange(splitLine, 1, splitLine.length);

                if (commandName.equals("execute_script")) {
                    var command = new ExecuteScriptCommand(provider, client, recDepth);
                    command.execute(args);
                }
                if (commandName.equals("exit")) {
                    var command = new ExitCommand(provider, client);
                    command.execute(args);
                }

                var command = commandManager.get(commandName);

                if (command.isEmpty()) {
                    printer.print("Invalid command");
                    continue;
                }

                var validateRequest = new ValidationRequest(command.get().name(), args);
                var validateResponse = (ValidationResponse) client.sendAndReceive(validateRequest);

                if (validateResponse.status() != StatusCode.OK) {
                    printer.print(validateResponse.message());
                    continue;
                }

                var commandRequest = new CommandRequest(command.get().name(), args);
                if (command.get().modelRequired()) {
                    commandRequest.setModel(new FlatParser(scanner, printer).parseFlat());
                }

                var commandResponse = (CommandResponse) client.sendAndReceive(commandRequest);
                printer.print(commandResponse.message());

            } catch (InterruptCommandException e) {
                printer.print("\nExited\n");
            } catch (NoSuchElementException e) {
                printer.print("EOF");
                break;
            } catch (InvalidArgsException e) {
                printer.print("Invalid args");
            } catch (RecursionException e) {
                printer.print("Recursion depth exceeded!");
                break;
            } catch (ExitException e) {
                provider.closeScanner();
                provider.getPrinter().print("Program has finished. Good luck!");
                break;
            }
        }
    }
}


