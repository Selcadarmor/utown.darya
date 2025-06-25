package com.example.Utown.dto.clientDto;;

import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.model.UserType.Client;

public class ClientMapper {

    public static void updateEntity(Client client, UserProfileUpdateDto dto) {
        client.setUsername(dto.getUsername());
        client.setFullName(dto.getFullName());
        client.setDefaultAddress(dto.getDefaultAddress());
    }
}

