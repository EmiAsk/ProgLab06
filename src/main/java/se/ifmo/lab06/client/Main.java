package se.ifmo.lab06.client;

import se.ifmo.lab06.client.manager.CommandManager;
import se.ifmo.lab06.client.parser.CommandParser;
import se.ifmo.lab06.shared.util.CLIPrinter;
import se.ifmo.lab06.shared.util.IOProvider;
import se.ifmo.lab06.shared.util.Printer;
import se.ifmo.lab06.shared.dto.request.GetCommandsRequest;
import se.ifmo.lab06.shared.dto.response.GetCommandsResponse;

import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static final String PROPS = "client.properties";

    public static void main(String[] args) {

        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPS)) {
            var props = new Properties();
            props.load(stream);
            var host = props.getProperty("HOST");
            var port = Integer.parseInt(props.getProperty("PORT"));
            Scanner scanner = new Scanner(System.in);
            Printer printer = new CLIPrinter();
            IOProvider provider = new IOProvider(scanner, printer);

            try (var client = Client.connect(host, port)) {
                var response = client.sendAndReceive(new GetCommandsRequest());
                CommandManager.register(((GetCommandsResponse) response).commands());
                var manager = new CommandManager(client, provider, 0);
                var parser = new CommandParser(client, manager, provider, 0);
                parser.run();
            }
        } catch (Exception e) {
            System.out.printf("Error occurred:\n%s", e.getMessage());
            System.exit(-1);
        }
    }
}
