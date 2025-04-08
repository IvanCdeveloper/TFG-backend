package com.example.tienda_reparaciones.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Extrae el token JWT de la cabecera Authoritation de la petición HTTP
 */
@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Permitir acceso sin validación a Swagger y API Docs
        if (requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/webjars/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.extractToken(request);

        if (this.tokenProvider.isValidToken(token)) {
            String username = this.tokenProvider.getUsernameFromToken(token);

            // Cargar usuario desde la base de datos
            UserDetails user = this.userDetailsService.loadUserByUsername(username);

            // Crear la autenticación
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            // Establecer el usuario autenticado en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); //Quitamos el Bearer y nos quedamos con el token
        }
        return null;
    }
}
