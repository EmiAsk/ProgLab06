package se.ifmo.lab06.shared.exception;

public class InvalidArgsException extends Exception {
    public InvalidArgsException(String message) {
        super(message);
    }

    public InvalidArgsException() {
        super("Invalid arguments");
    }
}
