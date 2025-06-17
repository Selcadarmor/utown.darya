package com.example.Utown.controller;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import com.example.Utown.enumFiles.Roles;
import com.example.Utown.service.AuthService;
import com.example.Utown.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class MainController {
    AuthService authService;
    RefreshTokenService refreshTokenService;


    @PostMapping("/auth")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, Roles.USER.toString());
    }

    @PostMapping("/register/restaurant")
    public ResponseEntity<String> registerRestaurant(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, Roles.RESTAURANT_ADMIN.toString());
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, Roles.ADMIN.toString());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTResponse> refreshToken(@RequestBody String refreshToken) {
        try {
            JWTResponse jwtResponse = refreshTokenService.refreshToken(refreshToken);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTResponse("",""));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String refreshToken) {
        try {
            refreshTokenService.logoutUserByRefreshToken(refreshToken);
            return ResponseEntity.ok("logout");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}


