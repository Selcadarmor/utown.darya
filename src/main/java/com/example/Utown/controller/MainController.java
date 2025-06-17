package com.example.Utown.controller;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import com.example.Utown.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class MainController {
    AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    // Пример в AuthController
    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, "ROLE_USER");
    }

    @PostMapping("/register/restaurant")
    public ResponseEntity<String> registerRestaurant(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, "ROLE_RESTAURANT");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody JWTRequest request) {
        return authService.createNewUser(request, "ROLE_ADMIN");
    }

}


