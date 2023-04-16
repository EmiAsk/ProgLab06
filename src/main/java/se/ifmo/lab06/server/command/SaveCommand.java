package se.ifmo.lab06.server.command;

import se.ifmo.lab06.server.exception.InvalidArgsException;
import se.ifmo.lab06.server.manager.CollectionManager;
import se.ifmo.lab06.server.util.IOProvider;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCommand extends Command {
    public SaveCommand(IOProvider provider, CollectionManager collection) {
        super("save", "сохранить коллекцию в файл", provider, collection);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args, 0);
        try (FileWriter fileWriter = new FileWriter(collection.getFileName())) {
            fileWriter.write(collection.dump());
            fileWriter.flush();
            provider.getPrinter().printf("Collection dumped to file (%s) successfully.\n", System.getenv("FILENAME"));
        } catch (FileNotFoundException e) {
            provider.getPrinter().print("File not found or access denied (write)");
        } catch (IOException e) {
            provider.getPrinter().print("Something went wrong while writing.");
        }
    }
}
