package com.example.Utown.service.UserTypeService;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.model.Address;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
        Client findByUsername(String username);
        Client getById(Long id);
        ClientInfoDto getClientById(Long clientId);
        Page<ClientDetailsDto> getAllClients(String query, Boolean isActive, Pageable pageable);
        List<AddressDto> getAddressesByClient();
        Client getCurrentClient();
        List<RestaurantForClientDto> getFavoriteRestaurants();
        void save(ClientRegistrationDto dto, Roles roleName);
        Address saveAddressForClient(AddressDto dto);
        void addFavoriteRestaurant(Long restaurantId);
        ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto);
        void updateClientActiveStatus(Long id, Boolean active);
        void removeFavoriteRestaurant(Long restaurantId);
        void deleteClient(Long id);
}



