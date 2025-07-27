package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User; // Importe sua entidade User
import br.com.idus.chronos.dto.out.auth.TokenPair;
import br.com.idus.chronos.repository.UserRepository; // Importe seu repositório
import br.com.idus.chronos.service.exceptions.InvalidJwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor // Lombok injetará o UserRepository automaticamente
public class JwtService {

    private final UserRepository userRepository; // Nova dependência para buscar o User completo

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }


    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtExpirationMs, new HashMap<>());
    }


    public String generateRefreshToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        return generateToken(authentication, refreshExpirationMs, claims);
    }


    private String generateToken(Authentication authentication, long expirationInMs, Map<String, Object> extraClaims) {
        User userPrincipal;
        Object principal = authentication.getPrincipal();


        if (principal instanceof User) {
            // Cenário ideal (ex: fluxo de refresh token bem implementado)
            userPrincipal = (User) principal;
        } else if (principal instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) principal;
            userPrincipal = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new InvalidJwtAuthenticationException("Usuário não encontrado para geração do token."));
        } else {
            throw new IllegalArgumentException("Tipo de 'principal' não suportado para geração de token: " + principal.getClass().getName());
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(extraClaims)
                .subject(userPrincipal.getId().toString())
                .claim("email", userPrincipal.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String emailInToken = extractEmail(token);
        return emailInToken != null && emailInToken.equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return extractEmail(token);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (InvalidJwtAuthenticationException e) {
            return false;
        }
    }


    public boolean isRefreshToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return "refresh".equals(claims.get("tokenType"));
        } catch (InvalidJwtAuthenticationException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token JWT expirado ou inválido: {}", e.getMessage());
            throw new InvalidJwtAuthenticationException("Token JWT expirado ou inválido.");
        }
    }

    private SecretKey getSignInKey() {

        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
