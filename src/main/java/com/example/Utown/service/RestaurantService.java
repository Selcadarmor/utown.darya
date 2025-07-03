package com.example.Utown.service;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;


import java.util.List;

public interface RestaurantService {
    List<RestaurantInfoDto> getAllRestaurants();
    RestaurantDetailsDto getRestaurantById(Long id);
    RestaurantDetailsDto createRestaurant(RestaurantCreateUpdateDto dto);
    RestaurantDetailsDto updateRestaurant(Long id, RestaurantCreateUpdateDto dto);
    void deleteRestaurant(Long id);
}
