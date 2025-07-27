package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.mappers.WorkJourneyMapper;
import br.com.idus.chronos.repository.WorkJourneyRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkJourneyServiceIMPL  implements  WorkJourneyService {
    private final  WorkJourneyRepository workJourneyRepository;

    public WorkJourneyServiceIMPL(WorkJourneyRepository workJourneyRepository) {
        this.workJourneyRepository = workJourneyRepository;
    }

    @Override
    public WorkJourney create(WorkJourneyCreateDTO dto) {
        return workJourneyRepository.save(WorkJourneyMapper.toEntity(dto));
    }
}
