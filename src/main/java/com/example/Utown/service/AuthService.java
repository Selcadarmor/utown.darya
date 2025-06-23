package com.example.Utown.service;

import com.example.Utown.dto.tokens.JWTRequest;
import com.example.Utown.dto.tokens.JWTResponse;
import com.example.Utown.dto.clientDto.ClientRegistrationDto;

public interface AuthService {
    JWTResponse createAuthToken(JWTRequest authRequest);

    void registerClient(ClientRegistrationDto clientRegistrationDto);
}

