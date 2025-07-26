package br.com.idus.chronos.dto.out;

public record PointResponseDTO(
        Long id,
        String pointEventType,
        UserBasicResponseDTO user
) {
}
