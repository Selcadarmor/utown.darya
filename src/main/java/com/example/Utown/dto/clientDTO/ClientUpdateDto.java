package com.example.Utown.dto.clientDTO;

import lombok.Data;




@Data
public class ClientUpdateDto {

    private boolean isActive;

    public ClientUpdateDto(boolean isActive) {
        this.isActive = isActive;
    }
}
