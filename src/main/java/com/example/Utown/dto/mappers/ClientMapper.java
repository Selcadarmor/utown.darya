package com.example.Utown.dto.mappers;;

import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.model.UserType.Client;

public class ClientMapper {

    public static void updateEntity(Client client, ClientProfileUpdateDto dto) {
        client.setUsername(dto.getUsername());
        client.setFullName(dto.getFullName());
        client.setDefaultAddress(dto.getDefaultAddress());
    }
}

