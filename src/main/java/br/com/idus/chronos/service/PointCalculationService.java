package br.com.idus.chronos.service;

import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;

import java.time.Instant;
import java.util.List;

public interface PointCalculationService {

    WorkDaySummaryResponseDTO calculateWorkDaySummary(List<Instant> sortedTimeEntries);
}
