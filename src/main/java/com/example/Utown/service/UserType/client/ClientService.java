package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientChangePasswordDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.model.UserType.Client;

import java.util.List;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        List<ClientInfoDto> getAllClients();
        ClientInfoDto getClientById(Long clientId);
        ClientInfoDto updateClient(Long id, ClientUpdateDto dto);
        void deleteClient(Long id);
        void changePassword(String username, ClientChangePasswordDto dto);
        Client updateClientProfile(Long clientId, String newFullName, List<AddressDto> updatedAddresses);
}



