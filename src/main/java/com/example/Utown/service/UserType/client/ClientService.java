package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.*;
import com.example.Utown.model.Address;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;

import java.util.List;
import java.util.Optional;

public interface ClientService {
        Optional<Client> findByUsername(String username);
        List<ClientDetailsDto> getAllClients();
        ClientInfoDto getClientById(Long clientId);
        void updateClient(Long id, ClientUpdateDto dto);
        void deleteClient(Long id);
        void save(ClientRegistrationDto dto, Roles roleName);
        ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto);
        Address saveAddressForClient(AddressDto dto);
        Client getCurrentClient();
        Address getAddressByDefaultAddress();
        List<AddressDto> getAddressesByClient();
        void deleteAddressForCLient(Long addressId);
}



