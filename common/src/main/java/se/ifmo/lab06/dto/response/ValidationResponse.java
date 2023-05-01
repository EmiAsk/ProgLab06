package se.ifmo.lab06.dto.response;

import se.ifmo.lab06.dto.StatusCode;
import se.ifmo.lab06.dto.request.ValidationRequest;

public record ValidationResponse(
        String message,
        StatusCode status
) implements Response {
    public ValidationResponse(String message) {
        this(message, StatusCode.OK);
    }
}
