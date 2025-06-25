package com.example.Utown.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDto {

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Please confirm the new password")
    private String confirmNewPassword;
}

