package se.ifmo.lab06.exception;

public class InvalidArgsException extends Exception {
    public InvalidArgsException(String message) {
        super(message);
    }

    public InvalidArgsException() {
        super("Invalid arguments");
    }
}
