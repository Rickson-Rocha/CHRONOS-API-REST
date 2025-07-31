package br.com.idus.chronos.controller;


import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.PointCreateDTO;
import br.com.idus.chronos.dto.out.PointResponseDTO;
import br.com.idus.chronos.dto.out.WorkDaySummaryResponseDTO;
import br.com.idus.chronos.service.PointService;
import br.com.idus.chronos.service.WorkJourneyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static br.com.idus.chronos.config.constant.ApiPaths.BASE_V1;

@RestController
@RequestMapping(BASE_V1+"/points")
public class PointController {

    private final PointService pointService;
    private final WorkJourneyService  workJourneyService;

    public PointController(PointService pointService, WorkJourneyService workJourneyService) {
        this.pointService = pointService;
        this.workJourneyService = workJourneyService;
    }


    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
    public ResponseEntity<PointResponseDTO> registerPoint(
            @RequestBody @Valid PointCreateDTO dto,
            @AuthenticationPrincipal User authenticatedUser, UriComponentsBuilder uriBuilder) {
        PointResponseDTO point = pointService.create(dto, authenticatedUser);
        var uri = uriBuilder.path(BASE_V1 + "/points/{id}").buildAndExpand(point.id()).toUri();
        return ResponseEntity.created(uri).body(point);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
    public ResponseEntity<WorkDaySummaryResponseDTO> getSummaryByDate(

            @RequestParam(name = "date", required = false) LocalDate date,
            @AuthenticationPrincipal User authenticatedUser) {

        LocalDate queryDate = (date == null) ? LocalDate.now() : date;

        WorkDaySummaryResponseDTO summary = workJourneyService.getWorkDaySummaryForUser(authenticatedUser, queryDate);

        return ResponseEntity.ok(summary);
    }
}