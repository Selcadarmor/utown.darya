package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.clientDto.ClientChangePasswordDto;
import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDto.ClientRegistrationDto;
import com.example.Utown.model.UserType.Client;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        void saveClient(ClientRegistrationDto dto, String roleName);
        void updateClientProfile(String currentUsername, ClientProfileUpdateDto dto);
        void changePassword(String username, ClientChangePasswordDto dto);

}


