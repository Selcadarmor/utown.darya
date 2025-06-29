package com.example.Utown.config;

import com.example.Utown.config.Utills.JWTUtils;
import com.example.Utown.exception.ExpireJwtTokenException;
import com.example.Utown.exception.InvalidJwtTokenException;
import com.example.Utown.model.enumFiles.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                // Проверка токена (подпись, срок действия и пр.)
                jwtUtils.validateToken(jwt, TokenType.ACCESS);

                // Получаем имя пользователя и роли из токена
                String username = jwtUtils.getUsernameFromToken(jwt, TokenType.ACCESS);
                Set<String> roles = jwtUtils.getRolesFromToken(jwt, TokenType.ACCESS);

                // Загружаем пользователя из базы, чтобы убедиться, что он активен, не заблокирован и пр.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Преобразуем роли в GrantedAuthority
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Создаём объект Authentication с ролями из токена
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpireJwtTokenException | InvalidJwtTokenException ex) {
                logger.warn("JWT token invalid or expired: " + ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}


