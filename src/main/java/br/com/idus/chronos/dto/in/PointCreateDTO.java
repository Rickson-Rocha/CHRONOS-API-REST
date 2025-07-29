package br.com.idus.chronos.dto.in;


import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record PointCreateDTO(
        @NotNull(message = "A data e hora da marcação são obrigatórias.")
        Instant timestamp
) {
}
