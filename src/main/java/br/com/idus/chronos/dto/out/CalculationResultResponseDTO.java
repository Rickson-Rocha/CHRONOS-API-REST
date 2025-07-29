package br.com.idus.chronos.dto.out;

import java.time.Duration;

public record CalculationResultResponseDTO(
        Duration totalWorkDuration,
        Duration totalBreakDuration
) {
}
