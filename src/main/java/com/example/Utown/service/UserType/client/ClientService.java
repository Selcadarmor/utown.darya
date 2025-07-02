package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.clientDto.ClientInfoDto;
import com.example.Utown.dto.clientDto.ClientUpdateDto;
import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.model.UserType.Client;

import java.util.List;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        List<ClientInfoDto> getAllClients();
        ClientInfoDto getClientById(Long clientId);
        ClientInfoDto updateClient(Long id, ClientUpdateDto dto);
        void deleteClient(Long id);
        void updateProfile(String currentUsername, UserProfileUpdateDto dto);
        void changePassword(String username, UserChangePasswordDto dto);

}


