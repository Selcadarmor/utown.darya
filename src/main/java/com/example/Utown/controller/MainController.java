package com.example.Utown.controller;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import com.example.Utown.model.Roles;
import com.example.Utown.service.AuthService;
import com.example.Utown.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/main")
@Tag(name = "Main", description = "Login,registration, refreshToken, logout")
public class MainController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth")
    @Operation(summary = "Login", description = "Login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register/user")
    @Operation(summary = "Register User", description = "Registration_user")
    public ResponseEntity<String> registerUser(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, Roles.USER.toString());
    }

    @PostMapping("/register/restaurant")
    @Operation(summary = "Register Restaurant", description = "Registration_restaurant")
    public ResponseEntity<String> registerRestaurant(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, Roles.RESTAURANT_ADMIN.toString());
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Register Admin", description = "Registration_admin")
    public ResponseEntity<String> registerAdmin(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, Roles.ADMIN.toString());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Token", description = "refreshToken_update")
    public ResponseEntity<JWTResponse> refreshToken(@RequestBody String refreshToken) {
        try {
            JWTResponse jwtResponse = refreshTokenService.refreshToken(refreshToken);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTResponse("", ""));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidate the refresh token and logout the user")
    public ResponseEntity<String> logout(@RequestBody String refreshToken) {
        try {
            refreshTokenService.logoutUserByRefreshToken(refreshToken);
            return ResponseEntity.ok("Logged out successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


