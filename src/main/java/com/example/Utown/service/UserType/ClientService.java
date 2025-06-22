package com.example.Utown.service.UserType;

import com.example.Utown.dto.СlientDto.ClientChangePasswordDto;
import com.example.Utown.dto.СlientDto.ClientProfileUpdateDto;
import com.example.Utown.dto.СlientDto.ClientRegistrationDto;
import com.example.Utown.model.UserType.Client;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        void saveClient(ClientRegistrationDto dto, String roleName);
        void updateClientProfile(String currentUsername, ClientProfileUpdateDto dto);
        void changePassword(String username, ClientChangePasswordDto dto);

}


