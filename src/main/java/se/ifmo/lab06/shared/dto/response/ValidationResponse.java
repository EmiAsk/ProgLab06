package se.ifmo.lab06.shared.dto.response;

import se.ifmo.lab06.shared.dto.StatusCode;
import se.ifmo.lab06.shared.dto.request.ValidationRequest;

public record ValidationResponse(
        String message,
        StatusCode status
) implements Response {
    public ValidationResponse(String message) {
        this(message, StatusCode.OK);
    }
}
