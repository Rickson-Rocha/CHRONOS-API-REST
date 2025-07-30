package br.com.idus.chronos.dto.mappers;

import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.out.CalculatedSummaryDTO;
import br.com.idus.chronos.dto.out.DurationResponseDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;
import br.com.idus.chronos.dto.out.WorkJourneyInfoResponseDTO;
import br.com.idus.chronos.enums.WorkDayStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class WorkDaySummaryMapper {


    public static WorkDaySummaryResponseDTO toResponseDTO(
            LocalDate date,
            WorkJourney workJourney,
            List<Instant> timeEntries,
            Duration totalWork,
            Duration totalBreak,
            Duration balance,
            WorkDayStatus status
    ) {

        WorkJourneyInfoResponseDTO journeyInfoDTO = toWorkJourneyInfoDTO(workJourney);
        CalculatedSummaryDTO summaryDTO = toCalculatedSummaryDTO(totalWork, totalBreak, balance, status);

        return new WorkDaySummaryResponseDTO(
                date,
                journeyInfoDTO,
                timeEntries,
                summaryDTO
        );
    }
    public static WorkDaySummaryResponseDTO toEmptyResponseDTO(LocalDate date, WorkJourney workJourney) {

        WorkJourneyInfoResponseDTO journeyInfoDTO = toWorkJourneyInfoDTO(workJourney);


        Duration expectedWorkload = Duration.ofMinutes(workJourney.getDaily_workload_minutes());
        Duration balance = expectedWorkload.negated();

        CalculatedSummaryDTO summaryDTO = new CalculatedSummaryDTO(
                toDurationDTO(Duration.ZERO),
                toDurationDTO(Duration.ZERO),
                formatBalance(balance),
                WorkDayStatus.NOT_STARTED.name()
        );

        return new WorkDaySummaryResponseDTO(
                date,
                journeyInfoDTO,
                Collections.emptyList(),
                summaryDTO
        );
    }

    private static CalculatedSummaryDTO toCalculatedSummaryDTO(Duration totalWork, Duration totalBreak, Duration balance, WorkDayStatus status) {
        return new CalculatedSummaryDTO(
                toDurationDTO(totalWork),
                toDurationDTO(totalBreak),
                formatBalance(balance),
                status.name()
        );
    }


    private static WorkJourneyInfoResponseDTO toWorkJourneyInfoDTO(WorkJourney workJourney) {
        if (workJourney == null) return null;

        String expectedWorkload = "PT" + (workJourney.getDaily_workload_minutes() / 60) + "H";
        return new WorkJourneyInfoResponseDTO(workJourney.getId(),workJourney.getDescription(), expectedWorkload);
    }


    private static DurationResponseDTO toDurationDTO(Duration duration) {
        if (duration == null) return new DurationResponseDTO(0, 0, "00:00");

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        String asString = String.format("%02d:%02d", hours, minutes);

        return new DurationResponseDTO(hours, minutes, asString);
    }


    private static String formatBalance(Duration balance) {
        if (balance == null || balance.isZero()) return "00:00";

        String sign = balance.isNegative() ? "-" : "+";
        Duration absoluteBalance = balance.abs();

        long hours = absoluteBalance.toHours();
        long minutes = absoluteBalance.toMinutesPart();

        return String.format("%s%02d:%02d", sign, hours, minutes);
    }
}
