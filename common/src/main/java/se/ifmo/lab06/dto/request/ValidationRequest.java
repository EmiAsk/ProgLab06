package se.ifmo.lab06.dto.request;

public record ValidationRequest(
        String commandName,
        String[] args
) implements Request {
}
