package br.com.idus.chronos.repository;

import br.com.idus.chronos.domain.WorkJourney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkJourneyRepository  extends JpaRepository<WorkJourney, Long> {
}
