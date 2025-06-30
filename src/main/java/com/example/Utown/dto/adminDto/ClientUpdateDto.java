package com.example.Utown.dto.adminDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;




@Data
public class ClientUpdateDto {

    @NotBlank
    private String username;

    @NotBlank
    @Size(max = 170)
    private String fullName;

    private boolean isActive;

    public ClientUpdateDto(String username, String fullName, boolean isActive) {
        this.username = username;
        this.fullName = fullName;
        this.isActive = isActive;
    }
}
