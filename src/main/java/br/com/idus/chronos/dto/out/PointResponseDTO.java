package br.com.idus.chronos.dto.out;

import java.time.Instant;

public record PointResponseDTO(
        Long id,
        Long userId,
        Instant timestamp
) {
}
