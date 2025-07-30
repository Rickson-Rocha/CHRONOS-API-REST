package br.com.idus.chronos.controller;


import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.out.PagedResponse;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import br.com.idus.chronos.dto.out.UserFullResponseDTO;
import br.com.idus.chronos.service.UserService;
import br.com.idus.chronos.service.WorkJourneyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static br.com.idus.chronos.config.constant.ApiPaths.BASE_V1;

@RestController
@RequestMapping(BASE_V1+"/users")
public class UserController {
    private final UserService userService;
    private final WorkJourneyService workJourneyService;

    public UserController(UserService userService, WorkJourneyService workJourneyService) {
        this.userService = userService;
        this.workJourneyService = workJourneyService;
    }


    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<UserBasicResponseDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO, UriComponentsBuilder uriBuilder){
        User user = userService.create(userCreateDTO);
        var uri = uriBuilder.path(BASE_V1 + "/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<PagedResponse<UserBasicResponseDTO>> findAll(Pageable pageable) {
        Page<UserBasicResponseDTO> page = userService.findAll(pageable);
        PagedResponse<UserBasicResponseDTO> response = new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees/{userId}")
    public ResponseEntity<UserFullResponseDTO> getUserSummary(
            @PathVariable Long userId,
            @RequestParam(name = "date", required = false) LocalDate date) {

        LocalDate queryDate = (date == null) ? LocalDate.now() : date;
        UserFullResponseDTO fullSummary = workJourneyService.getFullUserSummaryByIdAndDate(userId, queryDate);

        return ResponseEntity.ok(fullSummary);
    }

}
