package com.example.Utown.dto.clientDTO;

import lombok.AllArgsConstructor;
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
