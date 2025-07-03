package com.example.Utown.service.UserType.restaurant;

import com.example.Utown.dto.adminDto.RestaurantCreateUpdateDto;
import com.example.Utown.dto.adminDto.RestaurantDetailsDto;
import com.example.Utown.dto.adminDto.RestaurantInfoDto;
import com.example.Utown.dto.clientDto.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantInfoDto> getAllRestaurants();
    RestaurantDetailsDto getRestaurantById(Long id);
    RestaurantDetailsDto createRestaurant(RestaurantCreateUpdateDto dto);
    RestaurantDetailsDto updateRestaurant(Long id, RestaurantCreateUpdateDto dto);
    void deleteRestaurant(Long id);
    List<RestaurantDto> getAllForClientRestaurants();
    List<RestaurantDto> getRestaurantsSortedByFastestDelivery();
    long countRestaurantsByCategory(Long categoryId);
}
