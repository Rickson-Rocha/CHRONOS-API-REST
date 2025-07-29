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

        // Itera sobre os pares para calcular o TRABALHO (índices 0-1, 2-3, 4-5...)
        for (int i = 0; i < sortedTimeEntries.size() - 1; i += 2) {
            Instant startPeriod = sortedTimeEntries.get(i);
            Instant endPeriod = sortedTimeEntries.get(i + 1);
            totalWorkDuration = totalWorkDuration.plus(Duration.between(startPeriod, endPeriod));
        }

        // Itera sobre os pares para calcular a PAUSA (índices 1-2, 3-4, 5-6...)
        for (int i = 1; i < sortedTimeEntries.size() - 1; i += 2) {
            Instant startBreak = sortedTimeEntries.get(i);
            Instant endBreak = sortedTimeEntries.get(i + 1);
            totalBreakDuration = totalBreakDuration.plus(Duration.between(startBreak, endBreak));
        }

        return new CalculationResultResponseDTO(totalWorkDuration, totalBreakDuration);
    }

}
