package br.com.idus.chronos.dto.out;

public record UserFullResponseDTO(
        UserBasicResponseDTO user,
        WorkDaySummaryResponseDTO summary
) {
}