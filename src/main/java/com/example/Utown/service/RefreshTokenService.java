package com.example.Utown.service;

import com.example.Utown.dto.JWTResponse;
import com.example.Utown.dto.RefreshToken;
import com.example.Utown.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username, String jwtTokenString);
    Optional<RefreshToken> findByToken(String token);
    boolean isRefreshTokenExpired(RefreshToken token);
    void deleteByUser(User user);
    void deleteByToken(String token);
    JWTResponse refreshToken(String requestRefreshToken);
    void logoutUserByRefreshToken(String refreshTokenStr);
}
