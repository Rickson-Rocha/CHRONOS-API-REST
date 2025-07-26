package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;

public interface WorkJourneyService {
    WorkJourney create(WorkJourneyCreateDTO dto);
}
