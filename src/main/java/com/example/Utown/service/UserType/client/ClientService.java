package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.clientDto.ClientChangePasswordDto;
import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDto.ClientRegistrationDto;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;

import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        void saveClient(ClientRegistrationDto dto, Roles roleName);
        void updateClientProfile(String currentUsername, ClientProfileUpdateDto dto);
        void changePassword(String username, ClientChangePasswordDto dto);

}


