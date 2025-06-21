package com.example.Utown.service;

import com.example.Utown.config.Utills.JWTUtils;
import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<JWTResponse> createAuthToken(JWTRequest authRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshTokenStr = jwtUtils.generateRefreshToken(user);

        refreshTokenService.createRefreshToken(user.getUsername(), refreshTokenStr); //save in base

        return ResponseEntity.ok(new JWTResponse(accessToken, refreshTokenStr));
    }

    @Override
    public ResponseEntity<String> createNewUser(JWTRequest registrationRequest, String roleName) {
        if (userService.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("User already exists");
        }

        userService.saveUser(registrationRequest, roleName);
        return ResponseEntity.ok("User registered successfully");
    }
}

