package com.example.Utown.service.TokenService;

import com.example.Utown.dto.AdminDTO.AdminRegistrationDto;
import com.example.Utown.dto.tokens.JWTRequest;
import com.example.Utown.dto.tokens.JWTResponse;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;

public interface AuthService {
    JWTResponse createAuthToken(JWTRequest authRequest);
    void registration(ClientRegistrationDto clientRegistrationDto);
    void adminRegistration(AdminRegistrationDto dto);
}

