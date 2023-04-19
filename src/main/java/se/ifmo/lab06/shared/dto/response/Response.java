package se.ifmo.lab06.shared.dto.response;

import java.io.Serializable;

public sealed interface Response extends Serializable permits CommandResponse, GetCommandsResponse, ValidationResponse {
}
