package se.ifmo.lab06.client;

import se.ifmo.lab06.client.manager.CommandManager;
import se.ifmo.lab06.client.parser.CommandParser;
import se.ifmo.lab06.client.util.CLIPrinter;
import se.ifmo.lab06.client.util.IOProvider;
import se.ifmo.lab06.client.util.Printer;
import se.ifmo.lab06.shared.dto.request.GetCommandsRequest;
import se.ifmo.lab06.shared.dto.response.GetCommandsResponse;

import java.util.Scanner;

public class Main {

    private final static String FILENAME = System.getenv("FILENAME");
    private final static int PORT = 4445;
    private final static String HOST = "127.0.0.1";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Printer printer = new CLIPrinter();
        IOProvider provider = new IOProvider(scanner, printer);

        try (var client = Client.connect(HOST, PORT)) {
            var response = client.sendAndReceive(new GetCommandsRequest());
            System.out.println(((GetCommandsResponse) response).commands());
            CommandManager.register(((GetCommandsResponse) response).commands());
            var manager = new CommandManager(client, provider, 0);
            var parser = new CommandParser(client, manager, provider, 0);
            parser.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
//            System.out.println("Произошла ошибка при подключении.");
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
