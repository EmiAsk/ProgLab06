package se.ifmo.lab06.server;

import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.manager.CommandManager;
import se.ifmo.lab06.shared.dto.request.CommandRequest;
import se.ifmo.lab06.shared.dto.request.GetCommandsRequest;
import se.ifmo.lab06.shared.dto.request.ValidationRequest;
import se.ifmo.lab06.shared.dto.response.CommandResponse;
import se.ifmo.lab06.shared.dto.response.GetCommandsResponse;
import se.ifmo.lab06.server.util.CLIPrinter;
import se.ifmo.lab06.server.util.IOProvider;
import se.ifmo.lab06.server.util.Printer;
import se.ifmo.lab06.shared.dto.response.ValidationResponse;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private final static String FILENAME = "flats.json"; // System.getenv("FILENAME");

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(System.in);
        Printer printer = new CLIPrinter();
        IOProvider provider = new IOProvider(scanner, printer);

        CollectionManager collectionManager = CollectionManager.fromFile(FILENAME);
        CommandManager commandManager = new CommandManager(collectionManager, provider, 0);

        try (var server = new Server()) {
            while (true) {
                var pair = server.receiveRequest();
                var address = pair.getKey();
                var request = pair.getValue();

                if (request instanceof GetCommandsRequest getRequest) {
                    printer.printf("Запрос: %s получен\n", getRequest);
                    server.send(address, new GetCommandsResponse(commandManager.getCommands()));
                }

                if (request instanceof CommandRequest commandRequest) {
                    printer.printf("Команда: %s. Получена от: %s\n", commandRequest.name(), address);
                    server.send(address, commandManager.execute(commandRequest));
                }

                if (request instanceof ValidationRequest validationRequest) {
                    printer.printf("Запрос на валидацию: %s получен\n", validationRequest);
                    server.send(address, commandManager.validate(validationRequest));
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
//            printer.print("Произошла ошибка при подключении.");
//            System.exit(1);
        }


//        if (FILENAME == null) {
//            printer.print("Invalid filename env variable.");
//            return;
//        }
//        try {
//            CollectionManager collection = CollectionManager.fromFile(FILENAME);
//            printer.print("Collection loaded successfully.");
//            CommandManager commandManager = new CommandManager(collection, provider, 0);
//            CommandParser commandParser = new CommandParser(commandManager, provider, 0);
//            commandParser.run();
//        } catch (JsonParseException e) {
//            printer.print("Invalid JSON format or invalid input data.");
//        } catch (FileNotFoundException e) {
//            printer.print("File not found or access denied (read)");
//        }
    }
}
