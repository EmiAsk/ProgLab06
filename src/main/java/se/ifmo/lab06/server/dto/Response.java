package se.ifmo.lab06.server.dto;

public class Response {
    public enum Status {
        OK, ERROR
    }

    private final Status status;
    private final String message;

    public Response(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public Response(String message) {
        this(message, Status.OK);
    }

    public Status status() {
        return status;
    }

    public String message() {
        return message;
    }
}
