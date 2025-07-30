package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.out.UserFullResponseDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;
import br.com.idus.chronos.dto.out.WorkJourneyInfoResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface WorkJourneyService {
    WorkJourney create(WorkJourneyCreateDTO dto);
    WorkDaySummaryResponseDTO getWorkDaySummaryForUser(User user, LocalDate date);
    UserFullResponseDTO getFullUserSummaryByIdAndDate(Long userId, LocalDate date);
    List<WorkJourneyInfoResponseDTO> findAll();
}

