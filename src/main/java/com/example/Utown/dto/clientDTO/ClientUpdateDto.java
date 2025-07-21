package com.example.Utown.dto.clientDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ClientUpdateDto {

    private Boolean active;

    public ClientUpdateDto(Boolean active) {
        this.active = active;
    }
}
