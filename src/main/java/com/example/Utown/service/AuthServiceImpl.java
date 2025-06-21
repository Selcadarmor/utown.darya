package com.example.Utown.service;

import com.example.Utown.config.Utills.JWTUtils;
import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import com.example.Utown.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JWTResponse createAuthToken(JWTRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        refreshTokenService.createRefreshToken(user.getUsername(), refreshToken);

        return new JWTResponse(accessToken, refreshToken);
    }

    @Override
    public void createNewUser(JWTRequest registrationRequest, String roleName) {
        if (userService.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(registrationRequest.getUsername());
        }

        userService.saveUser(registrationRequest, roleName);
    }
}



