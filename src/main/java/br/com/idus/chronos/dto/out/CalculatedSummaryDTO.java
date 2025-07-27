package br.com.idus.chronos.dto.out;

public record CalculatedSummaryDTO(
        DurationResponseDTO totalWork,
        DurationResponseDTO totalBreak,
        String balance,
        String status
) {
}
