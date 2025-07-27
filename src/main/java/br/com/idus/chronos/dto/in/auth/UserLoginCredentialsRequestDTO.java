package br.com.idus.chronos.dto.in.auth;

import jakarta.validation.constraints.NotBlank;

public record UserLoginCredentialsRequestDTO(
        @NotBlank(message = "email é obrigatório")
        String email,

        @NotBlank(message = "senha é obrigatório")
        String password
) {
}
