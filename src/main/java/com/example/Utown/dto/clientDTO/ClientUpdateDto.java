package com.example.Utown.dto.clientDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ClientUpdateDto {

    private boolean isActive;

    public ClientUpdateDto(boolean isActive) {
        this.isActive = isActive;
    }
}
