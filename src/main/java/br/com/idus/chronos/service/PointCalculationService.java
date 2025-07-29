package br.com.idus.chronos.service;

import br.com.idus.chronos.dto.out.CalculationResultResponseDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;

import java.time.Instant;
import java.util.List;

public interface PointCalculationService {

    CalculationResultResponseDTO calculateDurations(List<Instant> sortedTimeEntries);
}
