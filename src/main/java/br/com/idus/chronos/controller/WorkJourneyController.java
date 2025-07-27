package br.com.idus.chronos.controller;


import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import br.com.idus.chronos.service.WorkJourneyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import static br.com.idus.chronos.config.constant.ApiPaths.BASE_V1;

@RestController
@RequestMapping(BASE_V1+"/work-journeys")
public class WorkJourneyController {

    private final  WorkJourneyService workJourneyService;

    public WorkJourneyController(WorkJourneyService workJourneyService) {
        this.workJourneyService = workJourneyService;
    }


    @PostMapping
    public ResponseEntity<UserBasicResponseDTO> create(@Valid @RequestBody WorkJourneyCreateDTO workJourneyCreateDTO, UriComponentsBuilder uriBuilder){
        WorkJourney workJourney = workJourneyService.create(workJourneyCreateDTO);
        var uri = uriBuilder.path(BASE_V1 + "/work-journeys/{id}").buildAndExpand(workJourney.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
