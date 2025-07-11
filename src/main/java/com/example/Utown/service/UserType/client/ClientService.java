package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.model.Address;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;

import java.util.List;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        List<ClientInfoDto> getAllClients();
        ClientInfoDto getClientById(Long clientId);
        void updateClient(Long id, ClientUpdateDto dto);
        void deleteClient(Long id);
        void save(ClientRegistrationDto dto, Roles roleName);
        void updateClientProfile(String username, ClientProfileUpdateDto dto);
        void saveAddressForClient(String username, AddressDto dto);
        Client getCurrentClient();
        Address getAddressByDefaultAddress();
        void deleteAddressForCLient(Long addressId, String username);
}



