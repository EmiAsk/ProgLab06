package se.ifmo.lab06.dto.response;

import se.ifmo.lab06.dto.CommandDTO;
import se.ifmo.lab06.dto.StatusCode;

import java.util.List;

public record GetCommandsResponse(
        List<CommandDTO> commands,
        StatusCode status
) implements Response {
    public GetCommandsResponse(List<CommandDTO> commands) {
        this(commands, StatusCode.OK);
    }
}
