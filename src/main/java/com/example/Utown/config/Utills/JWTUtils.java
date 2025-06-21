package com.example.Utown.config.Utills;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.Utown.model.enumFiles.TokenType;

@Component
public class JWTUtils {

    private final JWTProperties jwtProperties;

    public JWTUtils(JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private Key getAccessSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getAccessSecret().getBytes());
    }

    private Key getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getRefreshSecret().getBytes());
    }

    private Key getSigningKey(TokenType type) {
        return switch (type) {
            case ACCESS -> getAccessSigningKey();
            case REFRESH -> getRefreshSigningKey();
        };
    }

    public String generateAccessToken(UserDetails userDetails) {
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpirationMs()))
                .signWith(getAccessSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMs()))
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token, TokenType type) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
        public Set<String> getRolesFromToken(String token, TokenType type) {
        return new HashSet<>((List<String>) Jwts.parserBuilder()
                .setSigningKey(getSigningKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles"));
    }


    public boolean validateToken(String token, TokenType type) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(type))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT (" + type + "): " + e.getMessage());
            return false;
        }
    }
}







