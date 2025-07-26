package br.com.idus.chronos.dto.customerros;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Field validation error details")
public class FieldMessage {
    @Schema(description = "Name of the field with error", example = "serialNumber")
    private String fieldName;

    @Schema(description = "Error message", example = "Número de série é obrigatório")
    private String message;
}
