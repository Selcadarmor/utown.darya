package com.example.Utown.service;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    JWTResponse createAuthToken(JWTRequest authRequest);
    void createNewUser(JWTRequest registrationRequest, String roleName);
}
