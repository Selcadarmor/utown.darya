package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.adminDto.ClientInfoDto;
import com.example.Utown.dto.adminDto.ClientUpdateDto;
import com.example.Utown.dto.adminDto.OrderShortDto;
import com.example.Utown.dto.clientDto.ClientChangePasswordDto;
import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.model.UserType.Client;

import java.util.List;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        List<ClientInfoDto> getAllClients();
        ClientInfoDto getClientById(Long id);
        ClientInfoDto updateClient(Long id, ClientUpdateDto dto);
        void deleteClient(Long id);
        List<OrderShortDto> getClientOrders(Long clientId);
        void changePassword(String username, ClientChangePasswordDto dto);
        Client updateClientProfile(Long clientId, String newFullName, List<AddressDto> updatedAddresses);
}


