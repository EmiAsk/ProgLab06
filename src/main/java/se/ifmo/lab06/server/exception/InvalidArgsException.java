package se.ifmo.lab06.server.exception;

public class InvalidArgsException extends Exception {
    public InvalidArgsException(String message) {
        super(message);
    }

    public InvalidArgsException() {
        super("Invalid args");
    }

}
