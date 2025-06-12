package com.example.demo.config;

import com.example.demo.dto.JWTResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider {

    private final JWTUtils jwtUtils;

    public JWTTokenProvider(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public JWTResponse createTokens(Authentication authentication) {
        String username = authentication.getName();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String accessToken = jwtUtils.generateJwtToken(username, 15 * 60 * 1000);
        String refreshToken = jwtUtils.generateJwtToken(username, 7 * 24 * 60 * 60 * 1000);

        return new JWTResponse(accessToken, refreshToken, "Bearer", username, roles);
    }
}


