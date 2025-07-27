package br.com.idus.chronos.dto.out.auth;

public record UserLoginCredentialsResponseDTO (
        String name,
        String email,
        Long id,
        String role,
        TokenPair token
){
}
