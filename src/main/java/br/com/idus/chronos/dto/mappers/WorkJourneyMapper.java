package br.com.idus.chronos.dto.mappers;

import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.out.WorkJourneyInfoResponseDTO;

public class WorkJourneyMapper {

    public static WorkJourney toEntity (WorkJourneyCreateDTO dto){
        WorkJourney workJourney = new WorkJourney();
        workJourney.setDescription(dto.description());
        workJourney.setDaily_workload_minutes(dto.daily_workload_minutes());
        workJourney.setMinimum_break_minutes(dto.minimum_break_minutes());
        return workJourney;
    }
    public static WorkJourneyInfoResponseDTO toResponseDTO (WorkJourney workJourney) {
        if (workJourney == null) {
            return null;
        }

        Integer totalMinutes = workJourney.getDaily_workload_minutes();
        String formattedWorkload = "00:00";

        if (totalMinutes != null && totalMinutes > 0) {
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;
            formattedWorkload = String.format("%02d:%02d", hours, minutes);
        }

        return new WorkJourneyInfoResponseDTO(
                workJourney.getDescription(),
                formattedWorkload
        );
    }
}
