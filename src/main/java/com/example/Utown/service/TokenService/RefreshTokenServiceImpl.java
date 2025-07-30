package com.example.Utown.service.TokenService;

import com.example.Utown.config.Utills.JWTProperties;
import com.example.Utown.config.Utills.JWTUtils;
import com.example.Utown.dto.tokens.JWTResponse;
import com.example.Utown.dto.tokens.RefreshToken;
import com.example.Utown.exception.ExpireJwtTokenException;
import com.example.Utown.exception.RefreshTokenNotFoundException;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.UserType.User;
import com.example.Utown.repository.RefreshTokenRepository;
import com.example.Utown.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTProperties jwtProperties;
    private final JWTUtils jwtUtils;

    @Override
    public RefreshToken createRefreshToken(String username, String jwtTokenString) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        RefreshToken token = RefreshToken.builder()
                .token(jwtTokenString)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expirationTime(LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getRefreshExpirationMs())))
                .build();

        return refreshTokenRepository.save(token);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpirationTime().isBefore(LocalDateTime.now());
    }

    @Transactional
    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }

     @Override
     public JWTResponse refreshToken(String requestRefreshToken) {
        RefreshToken refreshToken = findByToken(requestRefreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (isRefreshTokenExpired(refreshToken)) {
            deleteByToken(requestRefreshToken);
            throw new ExpireJwtTokenException();
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtUtils.generateAccessToken(user);
        String newRefreshToken = jwtUtils.generateRefreshToken(user);

        deleteByToken(requestRefreshToken);
        createRefreshToken(user.getUsername(), newRefreshToken);

        return new JWTResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void logoutUserByRefreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = findByToken(refreshTokenStr)
                .orElseThrow(RefreshTokenNotFoundException::new);

        deleteByUser(refreshToken.getUser());
    }
}

