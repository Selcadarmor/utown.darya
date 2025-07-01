package com.example.Utown.dto.clientDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientChangePasswordDto {

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Please confirm the new password")
    private String confirmNewPassword;
}

