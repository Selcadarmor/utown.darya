package com.example.Utown.service;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    public ResponseEntity<JWTResponse> createAuthToken(@RequestBody JWTRequest authRequest);
    public ResponseEntity<String> createNewUser(@RequestBody JWTRequest registrationUserDto);
}
