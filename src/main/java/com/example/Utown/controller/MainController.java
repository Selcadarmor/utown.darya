package com.example.Utown.controller;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import com.example.Utown.dto.clientDto.ClientRegistrationDto;
import com.example.Utown.service.AuthService;
import com.example.Utown.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Main", description = "Login, registration, refreshToken, logout")
public class MainController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest authRequest) {
        JWTResponse response = authService.createAuthToken(authRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register/client")
    @Operation(summary = "Register Client", description = "Registration for client users")
    public ResponseEntity<String> registerClient(@RequestBody ClientRegistrationDto dto) {
        authService.registerClient(dto);
        return ResponseEntity.ok("Client registered successfully");
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/register/admin")
//    @Operation(summary = "Register Admin", description = "Registration_admin")
//    public ResponseEntity<String> registerAdmin(@RequestBody JWTRequest request) {
//        authService.createNewUser(request, Roles.ROLE_ADMIN.name());
//        return ResponseEntity.ok("Admin registered successfully");
//    }

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

    @PreAuthorize("isAuthenticated()")
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



