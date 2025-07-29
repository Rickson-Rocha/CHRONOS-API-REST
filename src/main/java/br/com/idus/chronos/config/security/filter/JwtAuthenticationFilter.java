package br.com.idus.chronos.config.security.filter;

import br.com.idus.chronos.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String jwt = getJwtFromRequest(request);

        if (jwt == null) {
            logger.debug("JWT não encontrado no cabeçalho da requisição.");
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("JWT extraído: {}", jwt);

        final String userEmail = jwtService.extractUsername(jwt);
        logger.debug("Usuário extraído do JWT: {}", userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("Nenhuma autenticação encontrada no contexto de segurança. Validando token...");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.validateToken(jwt, userDetails)) {
                logger.info("Token JWT válido. Autenticando usuário: {}", userEmail);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Usuário autenticado e contexto de segurança atualizado.");
            } else {
                logger.warn("Token JWT inválido para o usuário: {}", userEmail);
            }
        } else {
            if (userEmail == null) {
                logger.warn("Usuário extraído do JWT é nulo.");
            } else {
                logger.debug("Contexto de segurança já possui autenticação.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            logger.debug("Cabeçalho Authorization presente com prefixo Bearer.");
            return authHeader.substring(7);
        }

        logger.debug("Cabeçalho Authorization ausente ou mal formatado.");
        return null;
    }
}
