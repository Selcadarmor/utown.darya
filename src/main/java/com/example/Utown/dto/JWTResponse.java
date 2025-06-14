package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
}


