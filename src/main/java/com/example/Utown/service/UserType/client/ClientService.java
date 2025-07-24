package com.example.Utown.service.UserType.client;

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
        Page<ClientDetailsDto> getAllClients(String query, Boolean isActive, Pageable pageable);
        ClientInfoDto getClientById(Long clientId);
        void updateClientActiveStatus(Long id, Boolean active);
        void deleteClient(Long id);
        void save(ClientRegistrationDto dto, Roles roleName);
        void addFavoriteRestaurant(Long restaurantId);
        List<RestaurantForClientDto> getFavoriteRestaurants();
        void removeFavoriteRestaurant(Long restaurantId);
        Client getCurrentClient();
        Address getAddressByDefaultAddress();
}



