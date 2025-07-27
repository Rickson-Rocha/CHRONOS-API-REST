package br.com.idus.chronos.dto.customerros;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Standard error response")
public class CustomError {

    @Schema(description = "Timestamp of the error", example = "2024-05-15T10:00:00Z")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private Integer status;

    @Schema(description = "Error description", example = "Invalid input data")
    private String error;

    @Schema(description = "Request path", example = "/api/v1/machines")
    private String path;


}
