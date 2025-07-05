package com.example.Utown.service;

import com.example.Utown.dto.clientDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;


import java.util.List;

public interface RestaurantService {
    List<RestaurantInfoDto> getAllRestaurants();
    RestaurantDetailsDto getRestaurantById(Long id);
    RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto);
    RestaurantDetailsDto updateRestaurant(Long id, RestaurantUpdateDto dto);
    void deleteRestaurant(Long id);
    List<RestaurantForClientDto> getAllRestaurantsForClient();
    List<RestaurantForClientDto> getRestaurantsByCategoryId(Long categoryId);
    List<RestaurantForClientDto> getAllRestaurantsSortedByDeliveryTime();

}
