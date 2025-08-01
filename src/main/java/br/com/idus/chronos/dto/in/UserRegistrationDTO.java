package br.com.idus.chronos.dto.in;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegistrationDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O CPF é obrigatório.")
        String cpf,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        String password,

        @NotNull(message = "Tipo de jornada de trabalho é obrigatório")
        Long workJourneyId
) {}