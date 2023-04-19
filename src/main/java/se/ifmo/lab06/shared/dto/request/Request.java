package se.ifmo.lab06.shared.dto.request;


import java.io.Serializable;

public sealed interface Request extends Serializable permits ValidationRequest, CommandRequest, GetCommandsRequest {
}
