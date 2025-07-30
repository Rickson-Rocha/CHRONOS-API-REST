package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.out.auth.TokenPair;
import br.com.idus.chronos.repository.UserRepository;
import br.com.idus.chronos.service.exceptions.InvalidJwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;

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
        // LOG: Adicionado para verificar o tipo do principal
        log.debug("Iniciando geração de token. Tipo do Principal: {}", authentication.getPrincipal().getClass().getName());

        User userPrincipal;
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            userPrincipal = (User) principal;
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // LOG: Adicionado para verificar qual usuário está sendo buscado
            log.debug("Principal é UserDetails. Buscando usuário completo para o email: {}", userDetails.getUsername());
            userPrincipal = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new InvalidJwtAuthenticationException("Usuário não encontrado para geração do token."));
        } else {
            throw new IllegalArgumentException("Tipo de 'principal' não suportado para geração de token: " + principal.getClass().getName());
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        // LOG: Log principal da geração do token
        log.info("Gerando token para o usuário ID: {}. Email: {}. Expiração: {}", userPrincipal.getId(), userPrincipal.getEmail(), expiryDate);

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
        // LOG: Log de depuração para a validação
        log.debug("Validando token. Email no token: [{}]. Username do UserDetails: [{}].", emailInToken, userDetails.getUsername());
        boolean isValid = emailInToken != null && emailInToken.equals(userDetails.getUsername());
        if (!isValid) {
            log.warn("Validação falhou. Email do token não corresponde ao email do usuário carregado.");
        }
        return isValid;
    }

    public String extractUsername(String token) {
        return extractEmail(token); // Mantido por consistência, mas o 'sub' é o ID.
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
        }
        // LOG: Adicionados logs específicos para cada tipo de erro do JWT
        catch (ExpiredJwtException e) {
            log.warn("Token JWT expirado: {}", e.getMessage());
            throw new InvalidJwtAuthenticationException("Token JWT expirado.");
        } catch (UnsupportedJwtException e) {
            log.warn("Token JWT não suportado: {}", e.getMessage());
            throw new InvalidJwtAuthenticationException("Token JWT não suportado.");
        } catch (MalformedJwtException e) {
            log.warn("Token JWT malformado: {}", e.getMessage());
            throw new InvalidJwtAuthenticationException("Token JWT malformado.");
        } catch (SignatureException e) {
            log.warn("Assinatura do token JWT inválida: {}", e.getMessage());
            throw new InvalidJwtAuthenticationException("Assinatura do token JWT inválida.");
        } catch (IllegalArgumentException e) {
            log.warn("Argumento do token JWT inválido: {}", e.getMessage());
            throw new InvalidJwtAuthenticationException("Argumento do token JWT inválido.");
        }
    }

    private SecretKey getSignInKey() {
        // LOG: Log de depuração para verificar a chave sendo usada
        log.debug("Gerando chave de assinatura a partir do segredo.");
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}