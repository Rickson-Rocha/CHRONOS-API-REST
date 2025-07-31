package br.com.idus.chronos.controller;


import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.domain.WorkJourney;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.in.WorkJourneyCreateDTO;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import br.com.idus.chronos.dto.out.WorkJourneyInfoResponseDTO;
import br.com.idus.chronos.service.WorkJourneyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity <List<WorkJourneyInfoResponseDTO>> findAll(){
        List <WorkJourneyInfoResponseDTO> workJourneyInfoResponseDTOList = workJourneyService.findAll();
        return ResponseEntity.ok(workJourneyInfoResponseDTOList);
    }

}