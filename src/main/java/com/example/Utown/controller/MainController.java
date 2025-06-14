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

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody JWTRequest authRequest) {
        return authService.createNewUser(authRequest);
    }
}


