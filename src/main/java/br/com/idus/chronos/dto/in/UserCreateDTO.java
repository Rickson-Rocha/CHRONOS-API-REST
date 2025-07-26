package br.com.idus.chronos.dto.in;


import br.com.idus.chronos.enums.TypeUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,

        @NotBlank(message = "O CPF é obrigatório.")
        @Size(min = 11, max = 11, message = "O CPF deve conter exatamente 11 dígitos, sem pontuação.")
        String cpf,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        @NotNull(message = "O tipo de usuário (role) é obrigatório.")
        TypeUser role,

        @NotNull(message = "Tipo de jornada de trabalho é obrigatório ao cadastrar um colaborador")
        Long workJourneyId

) {
}
