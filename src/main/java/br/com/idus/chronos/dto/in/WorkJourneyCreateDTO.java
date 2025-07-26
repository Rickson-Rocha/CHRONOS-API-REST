package br.com.idus.chronos.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public record WorkJourneyCreateDTO(
        @NotBlank(message = "A descrição é obrigatória.")
        @Length(max = 255, message = "A descrição não pode exceder 255 caracteres.")
        String description,

        @NotNull(message = "A carga horária diária é obrigatória.")
        @Positive(message = "A carga horária deve ser um número positivo.")
        Integer daily_workload_minutes,

        @NotNull(message = "O tempo mínimo de pausa é obrigatório.")
        @PositiveOrZero(message = "O tempo de pausa não pode ser negativo.")
        Integer minimum_break_minutes
) {
}
