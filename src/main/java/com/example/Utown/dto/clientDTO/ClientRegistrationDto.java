package com.example.Utown.dto.clientDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRegistrationDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
}




