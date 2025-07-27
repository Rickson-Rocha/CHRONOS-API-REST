package br.com.idus.chronos.dto.out.auth;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
