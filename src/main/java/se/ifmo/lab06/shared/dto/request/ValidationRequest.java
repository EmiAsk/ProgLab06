package se.ifmo.lab06.shared.dto.request;

public record ValidationRequest(
        String commandName,
        String[] args
) implements Request {
}
