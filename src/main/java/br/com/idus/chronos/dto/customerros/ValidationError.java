package br.com.idus.chronos.dto.customerros;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "Validation error response (422 Unprocessable Entity)")
public class ValidationError extends CustomError {
    @Schema(description = "List of field validation errors")
    private final List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(Instant timestamp, int status, String message, String path) {
        super(timestamp, status, message, path);
    }

    public void addErro(String campo, String mensagem) {
        erros.add(new FieldMessage(campo, mensagem));
    }
}
