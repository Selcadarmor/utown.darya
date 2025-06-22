package com.example.Utown.service;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.dto.JWTResponse;

public interface AuthService {
    JWTResponse createAuthToken(JWTRequest authRequest);

    void registerClient(JWTRequest request);
}

