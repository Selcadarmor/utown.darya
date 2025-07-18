package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.model.Address;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
        Page<ClientDetailsDto> getAllClients(Pageable pageable);
        ClientInfoDto getClientById(Long clientId);
        void updateClientActiveStatus(Long id, Boolean active);
        void deleteClient(Long id);
        void save(ClientRegistrationDto dto, Roles roleName);
        ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto);
        Address saveAddressForClient(AddressDto dto);
        Client getCurrentClient();
        Address getAddressByDefaultAddress();
        List<AddressDto> getAddressesByClient();
        void deleteAddressForCLient(Long addressId);
}



