package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;

import java.time.LocalDate;

public interface WorkJourneyService {
    WorkJourney create(WorkJourneyCreateDTO dto);
    WorkDaySummaryResponseDTO getWorkDaySummaryForUser(User user, LocalDate date);
}
