package br.com.idus.chronos.dto.out;

public record UserBasicResponseDTO(
        Long id,

        String name,

        String cpf,

        String email,

        String role,

        WorkJourneyInfoResponseDTO workJourneyInfoDTO
) {
}
