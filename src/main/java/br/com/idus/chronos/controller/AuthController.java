package br.com.idus.chronos.controller;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.dto.in.auth.RefreshTokenRequest;
import br.com.idus.chronos.dto.in.auth.UserLoginCredentialsRequestDTO;
import br.com.idus.chronos.dto.out.auth.TokenPair;
import br.com.idus.chronos.dto.out.auth.UserLoginCredentialsResponseDTO;
import br.com.idus.chronos.repository.UserRepository;
import br.com.idus.chronos.service.JwtService;
import br.com.idus.chronos.service.exceptions.InvalidJwtAuthenticationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.idus.chronos.config.constant.ApiPaths.BASE_V1;

@RestController
@RequestMapping(BASE_V1+"/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;


    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository, UserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;

        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginCredentialsResponseDTO> login(@RequestBody @Valid UserLoginCredentialsRequestDTO request) {

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        System.out.println(authenticationToken);

        Authentication authenticated = authenticationManager.authenticate(authenticationToken);


        var user = userRepository.findByEmail(authenticated.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação"));


        TokenPair tokenPair = jwtService.generateTokenPair(authenticated);

        UserLoginCredentialsResponseDTO response = new UserLoginCredentialsResponseDTO(
                user.getName(),
                user.getEmail(),
                user.getId(),
                user.getRole().name(),
                tokenPair
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isRefreshToken(refreshToken) || !jwtService.isValidToken(refreshToken)) {
            throw new InvalidJwtAuthenticationException("Refresh token inválido ou expirado.");
        }

        String userEmail = jwtService.extractUsername(refreshToken);


        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidJwtAuthenticationException("Usuário do token não encontrado."));


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        TokenPair newTokenPair = jwtService.generateTokenPair(authentication);

        return ResponseEntity.ok(newTokenPair);
    }

}