package br.com.idus.chronos.dto.out;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record WorkDaySummaryResponseDTO(
        LocalDate date,
        WorkJourneyInfoResponseDTO workJourney,
        List<Instant> timeEntries,
        CalculatedSummaryDTO summary
) {
}
