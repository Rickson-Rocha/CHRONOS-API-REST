package br.com.idus.chronos.controller;


import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.UserCreateDTO;
import br.com.idus.chronos.dto.out.UserBasicResponseDTO;
import br.com.idus.chronos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import static br.com.idus.chronos.config.constant.ApiPaths.BASE_V1;

@RestController
@RequestMapping(BASE_V1+"/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserBasicResponseDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO, UriComponentsBuilder uriBuilder){
        User user = userService.create(userCreateDTO);
        var uri = uriBuilder.path(BASE_V1 + "/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
