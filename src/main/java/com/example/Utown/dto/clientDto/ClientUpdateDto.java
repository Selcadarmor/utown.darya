package com.example.Utown.dto.clientDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;




@Data
public class ClientUpdateDto {

    private boolean isActive;

    public ClientUpdateDto(boolean isActive) {
        this.isActive = isActive;
    }
}
