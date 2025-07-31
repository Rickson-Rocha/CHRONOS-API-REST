package br.com.idus.chronos.service;

import br.com.idus.chronos.dto.out.CalculationResultResponseDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class PointCalculationServiceImpl  implements PointCalculationService {
    @Override
    public CalculationResultResponseDTO calculateDurations(List<Instant> sortedTimeEntries) {
        if (sortedTimeEntries == null || sortedTimeEntries.size() < 2) {
            return new CalculationResultResponseDTO(Duration.ZERO, Duration.ZERO);
        }

        Duration totalWorkDuration = Duration.ZERO;
        Duration totalBreakDuration = Duration.ZERO;


        for (int i = 0; i < sortedTimeEntries.size() - 1; i += 2) {
            Instant startPeriod = sortedTimeEntries.get(i);
            Instant endPeriod = sortedTimeEntries.get(i + 1);
            totalWorkDuration = totalWorkDuration.plus(Duration.between(startPeriod, endPeriod));
        }


        for (int i = 1; i < sortedTimeEntries.size() - 1; i += 2) {
            Instant startBreak = sortedTimeEntries.get(i);
            Instant endBreak = sortedTimeEntries.get(i + 1);
            totalBreakDuration = totalBreakDuration.plus(Duration.between(startBreak, endBreak));
        }

        return new CalculationResultResponseDTO(totalWorkDuration, totalBreakDuration);
    }

}
