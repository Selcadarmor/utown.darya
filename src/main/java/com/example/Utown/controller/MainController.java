package com.example.Utown.controller;

import com.example.Utown.dto.tokens.JWTRequest;
import com.example.Utown.dto.tokens.JWTResponse;
import com.example.Utown.dto.tokens.RefreshTokenRequest;
import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.dto.userDto.UserRegistrationDto;
import com.example.Utown.service.AuthService;
import com.example.Utown.service.RefreshTokenService;
import com.example.Utown.service.UserService;
import com.example.Utown.service.UserType.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Main", description = "Login, registration, refreshToken, logout")
public class MainController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final ClientService clientService;

    @PostMapping("/registration-client")  //Passed
    @Operation(summary = "Register Client", description = "Registration for client users")
    public ResponseEntity<String> registerClient(@RequestBody UserRegistrationDto dto) {
        authService.registration(dto);
        return ResponseEntity.ok("Client registered successfully");
    }

    @PostMapping("/login") //Passed
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest authRequest) {
        JWTResponse response = authService.createAuthToken(authRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update") //Passed
    @Operation(summary = "Update client profile", description = "" )
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserProfileUpdateDto dto
    ) {
        clientService.updateProfile(user.getUsername(), dto);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/refresh_token") //Passed
    public ResponseEntity<JWTResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            JWTResponse jwtResponse = refreshTokenService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTResponse("", ""));
        }
    }


    @PutMapping("/change_password")//Passed
    @Operation(summary = "Change client password", description = "")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid UserChangePasswordDto dto,
            @AuthenticationPrincipal User user
    ) {
        clientService.changePassword(user.getUsername(), dto);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/logout")//Passed
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @Operation(summary = "Logout", description = "Invalidate the refresh token and logout the user")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request) {
        try {
            refreshTokenService.logoutUserByRefreshToken(request.getRefreshToken());
            return ResponseEntity.ok("Logged out successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}



