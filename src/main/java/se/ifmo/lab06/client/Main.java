package se.ifmo.lab06.client;

import com.google.gson.JsonParseException;
import se.ifmo.lab06.manager.CommandManager;
import se.ifmo.lab06.parser.CommandParser;
import se.ifmo.lab06.util.CLIPrinter;
import se.ifmo.lab06.util.IOProvider;
import se.ifmo.lab06.util.Printer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private final static String FILENAME = System.getenv("FILENAME");
    private final static int PORT = 5050;
    private final static String HOST = "127.0.0.1";

    public static String a() {
        return null;
    }
    public static void main(String[] args) {
        System.out.println(a() instanceof String);
    }
        Scanner scanner = new Scanner(System.in);
        Printer printer = new CLIPrinter();
        IOProvider provider = new IOProvider(scanner, printer);

        try (var client = Client.connect(HOST, PORT)){

        } catch (IOException e) {
            System.out.println("Произошла ошибка при подключении.");
            System.exit(1);
        }

        if (FILENAME == null) {
            printer.print("Invalid filename env variable.");
            return;
        }
        try {
            CollectionManager collection = CollectionManager.fromFile(FILENAME);
            printer.print("Collection loaded successfully.");
            CommandManager commandManager = new CommandManager(collection, provider, 0);
            CommandParser commandParser = new CommandParser(commandManager, provider, 0);
            commandParser.run();
        } catch (JsonParseException e) {
            printer.print("Invalid JSON format or invalid input data.");
        } catch (FileNotFoundException e) {
            printer.print("File not found or access denied (read)");
        }
    }
}
