package br.com.idus.chronos.dto.out;

public record DurationResponseDTO(
        long hours,
        long minutes,
        String asString
) {
}
