package se.ifmo.lab06.client.parser;

import se.ifmo.lab06.client.Client;
import se.ifmo.lab06.client.manager.CommandManager;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.util.Printer;
import se.ifmo.lab06.shared.util.ArgumentValidator;
import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.request.ValidationRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.ValidationResponse;
import se.ifmo.lab06.shared.exception.ExitException;
import se.ifmo.lab06.shared.exception.InterruptCommandException;
import se.ifmo.lab06.shared.exception.InvalidArgsException;
import se.ifmo.lab06.shared.exception.RecursionException;

import javax.naming.TimeLimitExceededException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandParser extends DefaultParser {
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
                printer.printf("\nEnter command:\n");
                String line = scanner.nextLine();
                String[] splitLine = line.strip().split("\s+");
                String commandName = splitLine[0].toLowerCase();
                String[] args = Arrays.copyOfRange(splitLine, 1, splitLine.length);

                var clientCommand = commandManager.getClientCommand(commandName);
                if (clientCommand.isPresent()) {
                    clientCommand.get().execute(args);
                    continue;
                }
                var serverCommand = commandManager.getServerCommand(commandName);
                if (serverCommand.isEmpty()) {
                    printer.print("Invalid command");
                    continue;
                }
                if (!ArgumentValidator.validate(serverCommand.get().args(), args)) {
                    printer.print("Invalid arguments");
                    continue;
                }
                var validateRequest = new ValidationRequest(serverCommand.get().name(), args);
                var validateResponse = (ValidationResponse) client.sendAndReceive(validateRequest);

                if (validateResponse.status() != StatusCode.OK) {
                    printer.print(validateResponse.message());
                    continue;
                }
                var commandRequest = new CommandRequest(serverCommand.get().name(), args);
                if (serverCommand.get().modelRequired()) {
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
            } catch (TimeLimitExceededException e) {
                provider.getPrinter().print(e.getMessage());
            } catch (IOException e) {
                provider.getPrinter().print("Error occurred while I/O");
            } catch (ClassNotFoundException e) {
                provider.getPrinter().print("Invalid response format from server");
            }
        }
    }
}


