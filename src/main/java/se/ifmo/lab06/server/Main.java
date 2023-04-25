package se.ifmo.lab06.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.manager.CommandManager;
import se.ifmo.lab06.server.network.Server;
import se.ifmo.lab06.shared.util.CLIPrinter;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.util.Printer;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {

        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties")) {
            Properties props = new Properties();
            props.load(stream);
            var filename = props.getProperty("FILENAME");
            var port = Integer.parseInt(props.getProperty("PORT"));

            Scanner scanner = new Scanner(System.in);
            Printer printer = new CLIPrinter();
            IOProvider provider = new IOProvider(scanner, printer);

            CollectionManager collectionManager = CollectionManager.fromFile(filename);
            CommandManager commandManager = new CommandManager(collectionManager, provider, 0);

            try (var server = new Server(commandManager, port)) {
                server.run();
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found or access denied (read):\n{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error. Something went wrong:\n{}", e.getMessage());
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
